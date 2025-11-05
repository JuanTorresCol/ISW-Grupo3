package icai.dtc.isw.dao;

import icai.dtc.isw.domain.Dificultad;
import icai.dtc.isw.domain.Ingrediente;
import icai.dtc.isw.domain.Receta;
import icai.dtc.isw.domain.Unidad;
import org.junit.jupiter.api.*;
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

    private Connection conn;
    private PreparedStatement pst;
    private ResultSet rs;

    @BeforeEach
    void setup() {
        conn = mock(Connection.class);
        pst = mock(PreparedStatement.class);
        rs = mock(ResultSet.class);
    }

    private Array mockSqlArray(String... values) throws Exception {
        Array sqlArray = mock(Array.class);
        when(sqlArray.getArray()).thenReturn(values);
        return sqlArray;
    }

    private Map<String, Ingrediente> ingredientesMapa() {
        Map<String, Ingrediente> m = new HashMap<>();
        m.put("huevo", new Ingrediente(2.0, Unidad.u));
        m.put("harina", new Ingrediente(250.0, Unidad.g));
        return m;
    }

    private String[] ingredientesPlano(Map<String, Ingrediente> map) {
        return map.entrySet().stream()
                .map(e -> e.getKey() + "," + e.getValue().getCantidad() + "," + e.getValue().getUnidad())
                .toArray(String[]::new);
    }


    //prueba que se cogen correctamente las recetas de la base de datos
    @Test
    void testGetRecetas() throws Exception {
        try (MockedStatic<ConnectionDAO> mocked = mockStatic(ConnectionDAO.class)) {
            ConnectionDAO cdao = mock(ConnectionDAO.class);
            mocked.when(ConnectionDAO::getInstance).thenReturn(cdao);
            when(cdao.getConnection()).thenReturn(conn);

            when(conn.prepareStatement("SELECT * FROM recetas")).thenReturn(pst);
            when(pst.executeQuery()).thenReturn(rs);

            when(rs.next()).thenReturn(true, true, false);

            when(rs.getString(1)).thenReturn("id-1", "id-2");
            when(rs.getString(2)).thenReturn("Tortilla", "Bizcocho");
            when(rs.getInt(3)).thenReturn(10, 45);
            when(rs.getDouble(4)).thenReturn(3.5, 6.7);
            when(rs.getString(5)).thenReturn("Huevos con patatas", "Bizcocho de vainilla");
            when(rs.getString(6)).thenReturn("FACIL", "MEDIO"); // <-- MEDIO

            // unidades y gramos en minúsculas (como tu enum Unidad)
            Array arr1 = mockSqlArray("huevo,2.0,unidades", "harina,250.0,gramos");
            Array arr2 = mockSqlArray("huevo,2.0,unidades");
            when(rs.getArray(6)).thenReturn(arr1, arr2); // si cambias el DAO a col 7, pon getArray(7)

            ArrayList<Receta> lista = RecetaDAO.getRecetas();

            assertEquals(2, lista.size());
            assertEquals("Tortilla", lista.get(0).getNombre());
            assertEquals(Dificultad.FACIL, lista.get(0).getDificultad());
        }
    }

    //prueba que se devuelve la receta correcta cuando el ID existe en la base de datos.
    @Test
    void testGetRecetaId() throws Exception {
        try (MockedStatic<ConnectionDAO> mocked = mockStatic(ConnectionDAO.class)) {
            ConnectionDAO cdao = mock(ConnectionDAO.class);
            mocked.when(ConnectionDAO::getInstance).thenReturn(cdao);
            when(cdao.getConnection()).thenReturn(conn);

            when(conn.prepareStatement("SELECT * FROM recetas WHERE id = ?")).thenReturn(pst);
            when(pst.executeQuery()).thenReturn(rs);

            when(rs.next()).thenReturn(true, false);
            when(rs.getString(1)).thenReturn("abc123");
            when(rs.getString(2)).thenReturn("Gazpacho");
            when(rs.getInt(3)).thenReturn(15);
            when(rs.getDouble(4)).thenReturn(4.2);
            when(rs.getString(5)).thenReturn("Frío y andaluz");
            when(rs.getString(6)).thenReturn("FACIL");

            Array arr = mockSqlArray("tomate,500.0,gramos"); // <-- gramos en minúsculas
            when(rs.getArray(6)).thenReturn(arr);

            Receta r = RecetaDAO.getRecetaId("abc123");

            assertNotNull(r);
            assertEquals("abc123", r.getId());
            assertEquals("Gazpacho", r.getNombre());
            verify(pst).setString(1, "abc123");
        }
    }
    //prueba que se encuentre la receta si esta esta en la base de datos
    @Test
    void testGetRecetaName() throws Exception {
        try (MockedStatic<ConnectionDAO> mocked = mockStatic(ConnectionDAO.class)) {
            ConnectionDAO cdao = mock(ConnectionDAO.class);
            mocked.when(ConnectionDAO::getInstance).thenReturn(cdao);
            when(cdao.getConnection()).thenReturn(conn);

            when(conn.prepareStatement("SELECT * FROM recetas WHERE nombre = ?")).thenReturn(pst);
            when(pst.executeQuery()).thenReturn(rs);

            when(rs.next()).thenReturn(true, false);
            when(rs.getString(1)).thenReturn("id-xyz");
            when(rs.getString(2)).thenReturn("Paella");
            when(rs.getInt(3)).thenReturn(40);
            when(rs.getDouble(4)).thenReturn(12.0);
            when(rs.getString(5)).thenReturn("Arroz con cosas");
            when(rs.getString(6)).thenReturn("DIFICIL");

            Array arr = mockSqlArray("arroz,400.0,gramos"); // <-- gramos en minúsculas
            when(rs.getArray(6)).thenReturn(arr);

            Receta r = RecetaDAO.getRecetaName("Paella");

            assertNotNull(r);
            assertEquals("Paella", r.getNombre());
            verify(pst).setString(1, "Paella");
        }
    }
    //prueba que cuando no exista la receta devuelva null
    @Test
    void testGetRecetaNameNotFound() throws Exception {
        try (MockedStatic<ConnectionDAO> mocked = mockStatic(ConnectionDAO.class)) {
            ConnectionDAO cdao = mock(ConnectionDAO.class);
            mocked.when(ConnectionDAO::getInstance).thenReturn(cdao);
            when(cdao.getConnection()).thenReturn(conn);

            when(conn.prepareStatement("SELECT * FROM recetas WHERE nombre = ?")).thenReturn(pst);
            when(pst.executeQuery()).thenReturn(rs);

            when(rs.next()).thenReturn(false);

            Receta r = RecetaDAO.getRecetaName("NoExiste");
            assertNull(r);
            verify(pst).setString(1, "NoExiste");
        }
    }

    //prueba que se registra correctamente una receta
    @Test
    void testRegisterReceta() throws Exception {
        try (MockedStatic<ConnectionDAO> mocked = mockStatic(ConnectionDAO.class)) {
            ConnectionDAO cdao = mock(ConnectionDAO.class);
            mocked.when(ConnectionDAO::getInstance).thenReturn(cdao);
            when(cdao.getConnection()).thenReturn(conn);

            when(conn.prepareStatement(
                    "INSERT INTO recetas (id, nombre, duracion, precio, descripcion, dificultad, ingredientes) VALUES (?,?,?,?,?,?,?)"
            )).thenReturn(pst);
            when(pst.getConnection()).thenReturn(conn);

            ArgumentCaptor<Object[]> cap = ArgumentCaptor.forClass(Object[].class);
            Array created = mock(Array.class);
            when(conn.createArrayOf(eq("VARCHAR"), cap.capture())).thenReturn(created);
            when(pst.executeUpdate()).thenReturn(1);

            Map<String, Ingrediente> ing = ingredientesMapa();
            Receta receta = new Receta("id-new", "Tarta Queso", Dificultad.MEDIO, 60, 8.50, "Clásica", ing);

            RecetaDAO.registerReceta(receta);

            verify(pst).setString(1, "id-new");
            verify(pst).setString(2, "Tarta Queso");
            verify(pst).setInt(3, 60);
            verify(pst).setDouble(4, 8.50);
            verify(pst).setString(5, "Clásica");
            verify(pst).setString(6, "MEDIO");
            verify(pst).setArray(eq(7), any(Array.class));
            verify(pst).executeUpdate();

            // Comprobación opcional del contenido del array
            Object[] enviado = cap.getValue();
            assertNotNull(enviado);
            List<String> vals = Arrays.stream(enviado).map(Object::toString).toList();
            assertTrue(vals.contains("huevo,2.0,unidades"));
            assertTrue(vals.contains("harina,250.0,gramos"));
        }
    }
}

