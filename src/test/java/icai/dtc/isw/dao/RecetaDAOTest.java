package icai.dtc.isw.dao;

import icai.dtc.isw.domain.Dificultad;
import icai.dtc.isw.domain.Ingrediente;
import icai.dtc.isw.domain.Receta;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RecetaDAOTest {

    //prueba que se recupera correctamente una receta por su ID, incluyendo sus ingredientes.
    @Test
    void testGetRecetaId() throws Exception {
        Connection conn = mock(Connection.class);
        PreparedStatement pst = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);
        Array sqlArray = mock(Array.class);

        try (MockedStatic<ConnectionDAO> mocked = mockStatic(ConnectionDAO.class)) {
            ConnectionDAO cdao = mock(ConnectionDAO.class);
            mocked.when(ConnectionDAO::getInstance).thenReturn(cdao);
            when(cdao.getConnection()).thenReturn(conn);

            when(conn.prepareStatement("SELECT * FROM recetas WHERE id = ?")).thenReturn(pst);
            when(pst.executeQuery()).thenReturn(rs);

            when(rs.next()).thenReturn(true, false);

            when(rs.getString(1)).thenReturn("ID123");
            when(rs.getString(2)).thenReturn("Tortilla");
            when(rs.getInt(3)).thenReturn(15);
            when(rs.getString(4)).thenReturn("Clásica");
            when(rs.getString(5)).thenReturn(Dificultad.MEDIO.name());
            when(rs.getArray(6)).thenReturn(sqlArray);
            when(sqlArray.getArray()).thenReturn(new String[]{
                    "huevos,2u",
                    "patatas,300g",
                    "cebolla, 1u"
            });

            Receta r = RecetaDAO.getRecetaId("ID123");

            assertNotNull(r);
            assertEquals("ID123", r.getId());
            assertEquals("Tortilla", r.getNombre());
            assertEquals(15, r.getDuracion());
            assertEquals("Clásica", r.getDescripcion());
            assertEquals(Dificultad.MEDIO, r.getDificultad());
            assertEquals(3, r.getIngredientes().size());
            assertTrue(r.getIngredientes().containsKey("huevos"));
            assertEquals("2u", r.getIngredientes().get("huevos").getCantidad());

            verify(pst).setString(1, "ID123");
            verify(pst).executeQuery();
        }
    }

    //prueba que se devuelve null cuando la receta con ese ID no existe.
    @Test
    void testGetRecetaIdNotFound() throws Exception {
        Connection conn = mock(Connection.class);
        PreparedStatement pst = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        try (MockedStatic<ConnectionDAO> mocked = mockStatic(ConnectionDAO.class)) {
            ConnectionDAO cdao = mock(ConnectionDAO.class);
            mocked.when(ConnectionDAO::getInstance).thenReturn(cdao);
            when(cdao.getConnection()).thenReturn(conn);

            when(conn.prepareStatement("SELECT * FROM recetas WHERE id = ?")).thenReturn(pst);
            when(pst.executeQuery()).thenReturn(rs);
            when(rs.next()).thenReturn(false);

            assertNull(RecetaDAO.getRecetaId("NOPE"));
            verify(pst).setString(1, "NOPE");
        }
    }

    //prueba que se devuelve null cuando no hay receta con ese nombre.
    @Test
    void testGetRecetaNameNotFound() throws Exception {
        Connection conn = mock(Connection.class);
        PreparedStatement pst = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        try (MockedStatic<ConnectionDAO> mocked = mockStatic(ConnectionDAO.class)) {
            ConnectionDAO cdao = mock(ConnectionDAO.class);
            mocked.when(ConnectionDAO::getInstance).thenReturn(cdao);
            when(cdao.getConnection()).thenReturn(conn);

            when(conn.prepareStatement("SELECT * FROM recetas WHERE nombre = ?")).thenReturn(pst);
            when(pst.executeQuery()).thenReturn(rs);
            when(rs.next()).thenReturn(false);

            assertNull(RecetaDAO.getRecetaName("NOPE"));
            verify(pst).setString(1, "NOPE");
        }
    }
    //prueba que se devuelve una lista de recetas correctas desde la base de datos.
    @Test
    void testGetDosRecetas() throws Exception {
        Connection conn = mock(Connection.class);
        PreparedStatement pst = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);
        Array array1 = mock(Array.class);
        Array array2 = mock(Array.class);

        try (MockedStatic<ConnectionDAO> mocked = mockStatic(ConnectionDAO.class)) {
            ConnectionDAO cdao = mock(ConnectionDAO.class);
            mocked.when(ConnectionDAO::getInstance).thenReturn(cdao);
            when(cdao.getConnection()).thenReturn(conn);

            when(conn.prepareStatement("SELECT * FROM recetas")).thenReturn(pst);
            when(pst.executeQuery()).thenReturn(rs);

            when(rs.next()).thenReturn(true, true, false);

            when(rs.getString(1)).thenReturn("R1", "R2");
            when(rs.getString(2)).thenReturn("Tortilla", "Pasta");
            when(rs.getInt(3)).thenReturn(15, 20);
            when(rs.getString(4)).thenReturn("Huevos y patata", "Tomate");
            when(rs.getString(5)).thenReturn(Dificultad.MEDIO.name(), Dificultad.FACIL.name());

            when(rs.getArray(6)).thenReturn(array1, array2);
            when(array1.getArray()).thenReturn(new String[]{"huevos,2ud", "patatas,300g"});
            when(array2.getArray()).thenReturn(new String[]{"pasta,200g", "tomate,100g"});

            ArrayList<Receta> lista = RecetaDAO.getRecetas();

            assertEquals(2, lista.size());
            assertEquals("R1", lista.get(0).getId());
            assertEquals("Tortilla", lista.get(0).getNombre());
            assertEquals(Dificultad.MEDIO, lista.get(0).getDificultad());

            assertEquals("R2", lista.get(1).getId());
            assertEquals("Pasta", lista.get(1).getNombre());
            assertEquals(Dificultad.FACIL, lista.get(1).getDificultad());
        }
    }
    //prueba que se inserta una receta con todos sus campos.
    @Test
    void testRegisterReceta() throws Exception {
        Connection conn = mock(Connection.class);
        PreparedStatement pst = mock(PreparedStatement.class);
        Array createdArray = mock(Array.class);

        try (MockedStatic<ConnectionDAO> mocked = mockStatic(ConnectionDAO.class)) {
            ConnectionDAO cdao = mock(ConnectionDAO.class);
            mocked.when(ConnectionDAO::getInstance).thenReturn(cdao);
            when(cdao.getConnection()).thenReturn(conn);

            when(conn.prepareStatement(
                    "INSERT INTO recetas (id, nombre, duracion, descripcion, dificultad, ingredientes) VALUES (?,?,?,?,?,?)"
            )).thenReturn(pst);

            when(pst.getConnection()).thenReturn(conn);
            when(conn.createArrayOf(eq("VARCHAR"), any())).thenReturn(createdArray);
            when(pst.executeUpdate()).thenReturn(1);

            Map<String, Ingrediente> ing = new LinkedHashMap<>();
            ing.put("huevos", new Ingrediente("huevos", "2u"));
            ing.put("patatas", new Ingrediente("patatas", "300g"));
            ing.put("cebolla", new Ingrediente("cebolla", "1u"));


            Receta r = new Receta(
                    "ID123",
                    "Tortilla",
                    Dificultad.MEDIO,
                    15,
                    "Clásica",
                    ing
            );

            RecetaDAO.registerReceta(r);

            verify(pst).setString(1, "ID123");
            verify(pst).setString(2, "Tortilla");
            verify(pst).setInt(3, 15);
            verify(pst).setString(4, "Clásica");
            verify(pst).setString(5, Dificultad.MEDIO.name());

            ArgumentCaptor<Object[]> cap = ArgumentCaptor.forClass(Object[].class);
            verify(conn).createArrayOf(eq("VARCHAR"), cap.capture());
            Object[] arr = cap.getValue();
            assertTrue(Arrays.stream(arr).anyMatch(o -> o.equals("huevos,2u")));
            assertTrue(Arrays.stream(arr).anyMatch(o -> o.equals("patatas,300g")));
            assertTrue(Arrays.stream(arr).anyMatch(o -> o.equals("cebolla,1u")));


            verify(pst).setArray(6, createdArray);
            verify(pst).executeUpdate();
        }
    }
}






