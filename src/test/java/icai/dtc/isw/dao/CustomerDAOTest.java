package icai.dtc.isw.dao;

import icai.dtc.isw.domain.Customer;
import icai.dtc.isw.utils.Util;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerDAOTest {

    //prueba que se construye correctamente un Customer a partir de los datos obtenidos por ID en la base de datos.
    @Test
    void testGetClienteId() throws Exception {
        Connection conn = mock(Connection.class);
        PreparedStatement pst = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);
        Array sqlArray = mock(Array.class);

        try (MockedStatic<ConnectionDAO> mocked = mockStatic(ConnectionDAO.class)) {
            ConnectionDAO cdao = mock(ConnectionDAO.class);
            mocked.when(ConnectionDAO::getInstance).thenReturn(cdao);
            when(cdao.getConnection()).thenReturn(conn);

            when(conn.prepareStatement("SELECT * FROM usuarios WHERE id = ?")).thenReturn(pst);
            when(pst.executeQuery()).thenReturn(rs);

            when(rs.next()).thenReturn(true, false);

            when(rs.getString("id")).thenReturn("ID123");
            when(rs.getString("name")).thenReturn("Maria");
            when(rs.getString("password")).thenReturn("secreta");
            when(rs.getString("gender")).thenReturn("M");
            when(rs.getInt("age")).thenReturn(21);

            when(rs.getArray("foodrestriction")).thenReturn(sqlArray);
            when(sqlArray.getArray()).thenReturn(new String[]{"gluten"});

            when(rs.getString("alimentosnocome")).thenReturn("pescado,marisco");

            Customer c = CustomerDAO.getClienteId("ID123");

            assertNotNull(c);
            assertEquals("Maria", c.getUserName());
            assertEquals("secreta", c.getUserPass());
            assertEquals("M", c.getUserGender());
            assertEquals(21, c.getUserAge());
            assertEquals(1, c.getIllegalFood().size());
            assertEquals("gluten", c.getIllegalFood().get(0));
            assertEquals(Arrays.asList("pescado", "marisco"), c.getAlimentosNoCome());

            verify(pst).setString(1, "ID123");
            verify(pst).executeQuery();
        }
    }
    //prueba que se devuelva null si el id de cliente no existe.
    @Test
    void testGetClienteIdNotFound() throws Exception {
        Connection conn = mock(Connection.class);
        PreparedStatement pst = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        try (MockedStatic<ConnectionDAO> mocked = mockStatic(ConnectionDAO.class)) {
            ConnectionDAO cdao = mock(ConnectionDAO.class);
            mocked.when(ConnectionDAO::getInstance).thenReturn(cdao);
            when(cdao.getConnection()).thenReturn(conn);

            when(conn.prepareStatement("SELECT * FROM usuarios WHERE id = ?")).thenReturn(pst);
            when(pst.executeQuery()).thenReturn(rs);
            when(rs.next()).thenReturn(false);

            assertNull(CustomerDAO.getClienteId("NOPE-ID"));
            verify(pst).setString(1, "NOPE-ID");
        }
    }
    //prueba que se devuelva null si el nombre de cliente no existe.
    @Test
    void testGetClienteNotFound() throws Exception {
        Connection conn = mock(Connection.class);
        PreparedStatement pst = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        try (MockedStatic<ConnectionDAO> mocked = mockStatic(ConnectionDAO.class)) {
            ConnectionDAO cdao = mock(ConnectionDAO.class);
            mocked.when(ConnectionDAO::getInstance).thenReturn(cdao);
            when(cdao.getConnection()).thenReturn(conn);

            when(conn.prepareStatement("SELECT * FROM usuarios WHERE name = ?")).thenReturn(pst);
            when(pst.executeQuery()).thenReturn(rs);
            when(rs.next()).thenReturn(false);

            assertNull(CustomerDAO.getCliente("NOPE"));
            verify(pst).setString(1, "NOPE");
        }
    }

    //prueba que se carga correctamente varios clientes desde la base de datos.
    @Test
    void testGetClientes() throws Exception {
        Connection conn = mock(Connection.class);
        PreparedStatement pst = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);
        Array array1 = mock(Array.class);
        Array array2 = mock(Array.class);

        try (MockedStatic<ConnectionDAO> mocked = mockStatic(ConnectionDAO.class)) {
            ConnectionDAO cdao = mock(ConnectionDAO.class);
            mocked.when(ConnectionDAO::getInstance).thenReturn(cdao);
            when(cdao.getConnection()).thenReturn(conn);

            when(conn.prepareStatement("SELECT * FROM usuarios")).thenReturn(pst);
            when(pst.executeQuery()).thenReturn(rs);

            when(rs.next()).thenReturn(true, true, false);

            // Fila 1
            when(rs.getString("id")).thenReturn("ID1", "ID2");
            when(rs.getString("name")).thenReturn("Maria", "Pablo");
            when(rs.getString("password")).thenReturn("pass1", "pass2");
            when(rs.getString("gender")).thenReturn("M", "H");
            when(rs.getInt("age")).thenReturn(21, 22);

            when(rs.getArray("foodrestriction")).thenReturn(array1, array2);
            when(array1.getArray()).thenReturn(new String[]{"gluten"});
            when(array2.getArray()).thenReturn(new String[]{"lactosa"});

            when(rs.getString("alimentosnocome")).thenReturn("pescado", "marisco");

            ArrayList<Customer> lista = new ArrayList<>();
            CustomerDAO.getClientes(lista);

            assertEquals(2, lista.size());
            assertEquals("Maria", lista.get(0).getUserName());
            assertEquals("Pablo", lista.get(1).getUserName());
            assertEquals(Arrays.asList("pescado"), lista.get(0).getAlimentosNoCome());
        }
    }
    //prueba que se añade correctamente un cliente en la base de datos
    @Test
    void testRegisterCliente() throws Exception {
        Connection conn = mock(Connection.class);
        PreparedStatement pst = mock(PreparedStatement.class);
        Array createdArray = mock(Array.class);

        try (MockedStatic<ConnectionDAO> mocked = mockStatic(ConnectionDAO.class)) {

            ConnectionDAO cdao = mock(ConnectionDAO.class);
            mocked.when(ConnectionDAO::getInstance).thenReturn(cdao);
            when(cdao.getConnection()).thenReturn(conn);

            when(conn.prepareStatement(
                    "INSERT INTO usuarios (id, name, password, gender, age, foodrestriction, alimentosnocome) VALUES (?,?,?,?,?,?,?)"
            )).thenReturn(pst);

            when(pst.getConnection()).thenReturn(conn);
            when(conn.createArrayOf(eq("varchar"), any())).thenReturn(createdArray);
            when(pst.executeUpdate()).thenReturn(1);

            Customer nuevo = new Customer(
                    "Maria",
                    "secreta",
                    "M",
                    21,
                    new ArrayList<>(Arrays.asList("gluten", "lactosa")),
                    new ArrayList<>(Arrays.asList("pescado"))
            );

            CustomerDAO.registerCliente(nuevo);

            verify(pst).setString(1, nuevo.getUserId());
            verify(pst).setString(2, "Maria");
            verify(pst).setString(3, "secreta");
            verify(pst).setString(4, "M");
            verify(pst).setInt(5, 21);

            ArgumentCaptor<Object[]> cap = ArgumentCaptor.forClass(Object[].class);
            verify(conn).createArrayOf(eq("varchar"), cap.capture());
            assertArrayEquals(new Object[]{"gluten", "lactosa"}, cap.getValue());

            verify(pst).setArray(6, createdArray);
            verify(pst).setString(7, "pescado"); // listToString(["pescado"])
            verify(pst).executeUpdate();
        }
    }
    //prueba que se actualiza en la base de datos todos los datos del cliente si se editan.
    @Test
    void testEditCliente() throws Exception {
        Connection conn = mock(Connection.class);
        PreparedStatement pst = mock(PreparedStatement.class);
        Array createdArray = mock(Array.class);

        try (MockedStatic<ConnectionDAO> mocked = mockStatic(ConnectionDAO.class)) {

            ConnectionDAO cdao = mock(ConnectionDAO.class);
            mocked.when(ConnectionDAO::getInstance).thenReturn(cdao);
            when(cdao.getConnection()).thenReturn(conn);

            when(conn.prepareStatement(
                    "UPDATE usuarios SET name = ?, password = ?, gender = ?, age = ?, foodrestriction = ?, alimentosnocome = ? WHERE id = ?"
            )).thenReturn(pst);

            when(pst.getConnection()).thenReturn(conn);
            when(conn.createArrayOf(eq("varchar"), any())).thenReturn(createdArray);
            when(pst.executeUpdate()).thenReturn(1);

            Customer editado = new Customer(
                    "ID123",
                    "Maria",
                    "nuevaPass",
                    "M",
                    22,
                    new ArrayList<>(Arrays.asList("gluten")),
                    new ArrayList<>(Arrays.asList("marisco"))
            );

            CustomerDAO.editCliente(editado);

            verify(pst).setString(1, "Maria");
            verify(pst).setString(2, "nuevaPass");
            verify(pst).setString(3, "M");
            verify(pst).setInt(4, 22);
            verify(conn).createArrayOf(eq("varchar"), any());
            verify(pst).setArray(5, createdArray);
            verify(pst).setString(6, "marisco");
            verify(pst).setString(7, "ID123");
            verify(pst).executeUpdate();
        }
    }
}



