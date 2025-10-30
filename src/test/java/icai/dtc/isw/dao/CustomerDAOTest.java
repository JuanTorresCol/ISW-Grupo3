package icai.dtc.isw.dao;

import icai.dtc.isw.domain.Customer;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class CustomerDAOTest{


    //prueba para que si se recibe un null no de error
    @Test
    void testToArrayList() {
        assertTrue(CustomerDAO.toArrayList(null).isEmpty());
    }
    //prueba que se capture correctamente la excepcion SQL
    @Test
    void testToArrayList_SQLException() throws Exception {
        Array sqlArray = mock(Array.class);
        when(sqlArray.getArray()).thenThrow(new SQLException("boom"));
        RuntimeException ex = assertThrows(RuntimeException.class, () -> CustomerDAO.toArrayList(sqlArray));
        assertTrue(ex.getMessage().contains("Error converting SQL Array"));
    }

    //Lectura correcta de la tabla
    //prueba que se ha encontrado al cliente
    @Test
    void testGetCustomerId() throws Exception {
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
            when(rs.getString(2)).thenReturn("Maria");
            when(rs.getString(3)).thenReturn("secreta");
            when(rs.getString(4)).thenReturn("M");
            when(rs.getInt(5)).thenReturn(21);
            when(rs.getArray(6)).thenReturn(sqlArray);
            when(sqlArray.getArray()).thenReturn(new String[]{"gluten"});
            when(rs.getString(7)).thenReturn("pescado");

            Customer c = CustomerDAO.getClienteId("ID123");

            assertNotNull(c);
            assertEquals("Maria", c.getUserName());
            assertEquals("secreta", c.getUserPass());
            assertEquals("M", c.getUserGender());
            assertEquals(21, c.getUserAge());
            assertEquals(1, c.getIllegalFood().size());
            assertEquals("gluten", c.getIllegalFood().getFirst());
            assertEquals("pescado", c.getAlimentosNoCome());

            verify(pst).setString(1, "ID123");
            verify(pst).executeQuery();
        }
    }

    // prueba que si no ha encontrado al cliente devuelva null
    @Test
    void testGetCustomerNotFound() throws Exception {
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

    // prueba que funciona con varios users
    @Test
    void testGetDifferentCustomers() throws Exception {
        Connection conn = mock(Connection.class);
        PreparedStatement pst = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        Array sqlArray1 = mock(Array.class);
        Array sqlArray2 = mock(Array.class);

        try (MockedStatic<ConnectionDAO> mocked = mockStatic(ConnectionDAO.class)) {
            ConnectionDAO cdao = mock(ConnectionDAO.class);
            mocked.when(ConnectionDAO::getInstance).thenReturn(cdao);
            when(cdao.getConnection()).thenReturn(conn);

            when(conn.prepareStatement("SELECT * FROM usuarios")).thenReturn(pst);
            when(pst.executeQuery()).thenReturn(rs);

            when(rs.next()).thenReturn(true, true, false);

            when(rs.getString(2)).thenReturn("Maria", "Pablo");  // 1ª llamada -> "Maria", 2ª -> "Pablo"
            when(rs.getString(3)).thenReturn("pass1", "pass2");
            when(rs.getString(4)).thenReturn("M", "H");
            when(rs.getInt(5)).thenReturn(21, 22);

            when(rs.getArray(6)).thenReturn(sqlArray1, sqlArray2);
            when(sqlArray1.getArray()).thenReturn(new String[]{"gluten"});
            when(sqlArray2.getArray()).thenReturn(new String[]{"lactosa"});

            when(rs.getString(7)).thenReturn("pescado", "marisco");

            ArrayList<Customer> lista = new ArrayList<>();
            CustomerDAO.getClientes(lista);

            assertEquals(2, lista.size());
            assertEquals("Maria", lista.get(0).getUserName());
            assertEquals("Pablo", lista.get(1).getUserName());

        }
    }


    //  prueba que funciona el registro del customer
    @Test
    void testRegisterCustomer() throws Exception {
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
            when(conn.createArrayOf(eq("VARCHAR"), any())).thenReturn(createdArray);
            when(pst.executeUpdate()).thenReturn(1);

            Customer nuevo = new Customer(
                    "Maria","secreta","M",21,
                    new ArrayList<>(Arrays.asList("gluten","lactosa")),
                    "pescado"
            );

            CustomerDAO.registerCliente(nuevo);

            verify(pst).setString(1, nuevo.getUserId()); // id generado desde userName
            verify(pst).setString(2, "Maria");
            verify(pst).setString(3, "secreta");
            verify(pst).setString(4, "M");
            verify(pst).setInt(5, 21);

            ArgumentCaptor<Object[]> cap = ArgumentCaptor.forClass(Object[].class);
            verify(conn).createArrayOf(eq("VARCHAR"), cap.capture());
            assertArrayEquals(new Object[]{"gluten","lactosa"}, cap.getValue());

            verify(pst).setArray(6, createdArray);
            verify(pst).setString(7, "pescado");
            verify(pst).executeUpdate();
        }
    }
}


