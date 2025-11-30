package icai.dtc.isw.dao;

import icai.dtc.isw.controler.RecetaControler;
import icai.dtc.isw.domain.Customer;
import icai.dtc.isw.domain.MenuSemanal;
import icai.dtc.isw.domain.Receta;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import icai.dtc.isw.utils.Util;

class CustomerDAOTest {


    //prueba obtener un cliente por su id
    @Test
    void testGetClienteId() throws Exception {
        Connection conn = mock(Connection.class);
        PreparedStatement pst = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);
        Array arrayFood = mock(Array.class);
        Array arrayFav = mock(Array.class);

        Receta recetaMock = mock(Receta.class);

        try (MockedStatic<ConnectionDAO> mockedConn = mockStatic(ConnectionDAO.class);
             MockedStatic<Util> mockedUtil = mockStatic(Util.class);
             MockedStatic<RecetaControler> mockedReceta = mockStatic(RecetaControler.class)) {

            ConnectionDAO cdao = mock(ConnectionDAO.class);
            mockedConn.when(ConnectionDAO::getInstance).thenReturn(cdao);
            when(cdao.getConnection()).thenReturn(conn);

            when(conn.prepareStatement("SELECT * FROM usuarios WHERE id = ?")).thenReturn(pst);
            when(pst.executeQuery()).thenReturn(rs);

            when(rs.next()).thenReturn(true, false);

            when(rs.getInt("puesto")).thenReturn(0);
            when(rs.getString("id")).thenReturn("ID123");
            when(rs.getString("name")).thenReturn("Maria");
            when(rs.getString("password")).thenReturn("abc");
            when(rs.getString("gender")).thenReturn("M");
            when(rs.getInt("age")).thenReturn(21);
            when(rs.getArray("foodrestriction")).thenReturn(arrayFood);
            when(rs.getString("alimentosnocome")).thenReturn("pescado,marisco");
            when(rs.getArray("favrecetas")).thenReturn(arrayFav);
            when(arrayFav.getArray()).thenReturn(new String[]{"R1"});

            mockedUtil.when(() -> Util.toArrayList(arrayFood)).thenReturn(new ArrayList<>(Arrays.asList("gluten")));
            mockedReceta.when(() -> RecetaControler.getRecetaId("R1")).thenReturn(recetaMock);

            Customer c = CustomerDAO.getClienteId("ID123");
            assertNotNull(c);

            assertEquals("Maria", c.getUserName());
            assertEquals("abc", c.getUserPass());
            assertEquals("M", c.getUserGender());
            assertEquals(21, c.getUserAge());
            assertEquals(Arrays.asList("gluten"), c.getIllegalFood());
            assertEquals(Arrays.asList("pescado", "marisco"), c.getAlimentosNoCome());
            assertEquals(0, c.getRecetasFav().size());

            verify(pst).setString(1, "ID123");
        }
    }


    //prueba que no se encuentra al cliente por el id
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

            Customer c = CustomerDAO.getClienteId("X999");
            assertNull(c);

            verify(pst).setString(1, "X999");
        }
    }


    //prueba a obtener cliente por nombre
    @Test
    void testGetClientePorNombre() throws Exception {
        Connection conn = mock(Connection.class);
        PreparedStatement pst = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);
        Array arrayFood = mock(Array.class);
        Array arrayFav = mock(Array.class);
        Array arrayMenu = mock(Array.class);

        Receta recetaMock = mock(Receta.class);

        try (MockedStatic<ConnectionDAO> mc = mockStatic(ConnectionDAO.class);
             MockedStatic<RecetaControler> mr = mockStatic(RecetaControler.class)) {

            ConnectionDAO cdao = mock(ConnectionDAO.class);
            mc.when(ConnectionDAO::getInstance).thenReturn(cdao);
            when(cdao.getConnection()).thenReturn(conn);

            when(conn.prepareStatement("SELECT * FROM usuarios WHERE name = ?")).thenReturn(pst);
            when(pst.executeQuery()).thenReturn(rs);

            when(rs.next()).thenReturn(true, false);

            when(rs.getInt("puesto")).thenReturn(0);
            when(rs.getString("id")).thenReturn("ID1");
            when(rs.getString("name")).thenReturn("Maria");
            when(rs.getString("password")).thenReturn("abc");
            when(rs.getString("gender")).thenReturn("M");
            when(rs.getInt("age")).thenReturn(21);

            // FOOD
            when(rs.getArray("foodrestriction")).thenReturn(arrayFood);
            when(arrayFood.getArray()).thenReturn(new String[]{"lactosa"});

            when(rs.getString("alimentosnocome")).thenReturn("carne");

            when(rs.getArray("favrecetas")).thenReturn(arrayFav);
            when(arrayFav.getArray()).thenReturn(new String[]{"R1"});
            mr.when(() -> RecetaControler.getRecetaId("R1")).thenReturn(recetaMock);

            when(rs.getArray("menusemanal")).thenReturn(arrayMenu);

            when(arrayMenu.getArray()).thenReturn(
                    new String[]{"R1","R2","R3","R4","R5","R6","R7","R8","R9","R10"}
            );
            mr.when(() -> RecetaControler.getRecetaId(anyString())).thenReturn(recetaMock);

            var res = CustomerDAO.getCliente("Maria");

            assertNotNull(res);
            assertEquals("Maria", res.getCustomer().getUserName());
            assertTrue(res.getMenu() instanceof MenuSemanal);
        }
    }



    //prueba a obtener varios clientes
    @Test
    void testGetClientes() throws Exception {
        Connection conn = mock(Connection.class);
        PreparedStatement pst = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);
        Array arrFood1 = mock(Array.class);
        Array arrFood2 = mock(Array.class);
        Array arrFav1 = mock(Array.class);
        Array arrFav2 = mock(Array.class);

        Receta recetaMock = mock(Receta.class);

        try (MockedStatic<ConnectionDAO> mocked = mockStatic(ConnectionDAO.class);
             MockedStatic<Util> mockedUtil = mockStatic(Util.class);
             MockedStatic<RecetaControler> mockedReceta = mockStatic(RecetaControler.class)) {

            ConnectionDAO cdao = mock(ConnectionDAO.class);
            mocked.when(ConnectionDAO::getInstance).thenReturn(cdao);
            when(cdao.getConnection()).thenReturn(conn);

            when(conn.prepareStatement("SELECT * FROM usuarios")).thenReturn(pst);
            when(pst.executeQuery()).thenReturn(rs);

            when(rs.next()).thenReturn(true, true, false);

            when(rs.getInt("puesto")).thenReturn(0, 0);
            when(rs.getString("id")).thenReturn("ID1", "ID2");
            when(rs.getString("name")).thenReturn("Maria", "Juan");
            when(rs.getString("password")).thenReturn("p1", "p2");
            when(rs.getString("gender")).thenReturn("M", "H");
            when(rs.getInt("age")).thenReturn(21, 20);

            mockedUtil.when(() -> Util.toArrayList(arrFood1)).thenReturn(new ArrayList<>(Arrays.asList("nueces")));
            mockedUtil.when(() -> Util.toArrayList(arrFood2)).thenReturn(new ArrayList<>(Arrays.asList("gluten")));

            when(rs.getArray("foodrestriction")).thenReturn(arrFood1, arrFood2);
            when(rs.getString("alimentosnocome")).thenReturn("carne", "pescado");
            when(rs.getArray("favrecetas")).thenReturn(arrFav1, arrFav2);

            mockedUtil.when(() -> Util.toArrayList(arrFav1)).thenReturn(new ArrayList<>(Arrays.asList("R1")));
            mockedUtil.when(() -> Util.toArrayList(arrFav2)).thenReturn(new ArrayList<>(Arrays.asList("R2")));
            mockedReceta.when(() -> RecetaControler.getRecetaId(anyString())).thenReturn(recetaMock);

            ArrayList<Customer> lista = new ArrayList<>();
            CustomerDAO.getClientes(lista);

            assertEquals(2, lista.size());
            assertEquals("Maria", lista.get(0).getUserName());
            assertEquals("Juan", lista.get(1).getUserName());
        }
    }


    // prueba a registra un cliente
    @Test
    void testRegisterCliente() throws Exception {
        Connection conn = mock(Connection.class);
        PreparedStatement pst = mock(PreparedStatement.class);
        Array arr1 = mock(Array.class);
        Array arr2 = mock(Array.class);

        try (MockedStatic<ConnectionDAO> mocked = mockStatic(ConnectionDAO.class)) {

            ConnectionDAO cdao = mock(ConnectionDAO.class);
            mocked.when(ConnectionDAO::getInstance).thenReturn(cdao);
            when(cdao.getConnection()).thenReturn(conn);

            when(conn.prepareStatement(anyString())).thenReturn(pst);

            when(pst.getConnection()).thenReturn(conn);
            when(conn.createArrayOf(eq("varchar"), any())).thenReturn(arr1, arr2);

            when(pst.executeUpdate()).thenReturn(1);

            Customer c = new Customer(
                    "ID9",
                    "Maria",
                    "abc",
                    "M",
                    21,
                    new ArrayList<>(Arrays.asList("gluten")),
                    new ArrayList<>(Arrays.asList("pescado")),
                    new ArrayList<>(),
                    80

            );

            assertTrue(CustomerDAO.registerCliente(c));

            verify(pst).setString(1, "ID9");
            verify(pst).setString(2, "Maria");
            verify(pst).setString(3, "abc");
            verify(pst).setString(4, "M");
            verify(pst).setInt(5, 21);
        }
    }



    //prueba que se edita el cliente
    @Test
    void testEditCliente() throws Exception {
        Connection conn = mock(Connection.class);
        PreparedStatement pst = mock(PreparedStatement.class);
        Array arr1 = mock(Array.class);
        Array arr2 = mock(Array.class);

        try (MockedStatic<ConnectionDAO> mocked = mockStatic(ConnectionDAO.class)) {

            ConnectionDAO cdao = mock(ConnectionDAO.class);
            mocked.when(ConnectionDAO::getInstance).thenReturn(cdao);
            when(cdao.getConnection()).thenReturn(conn);

            when(conn.prepareStatement(anyString())).thenReturn(pst);
            when(pst.getConnection()).thenReturn(conn);
            when(conn.createArrayOf(eq("varchar"), any())).thenReturn(arr1, arr2);
            when(pst.executeUpdate()).thenReturn(1);

            Customer c = new Customer(
                    "ID3",
                    "Maria",
                    "abc",
                    "M",
                    21,
                    new ArrayList<>(Arrays.asList("gluten")),
                    new ArrayList<>(Arrays.asList("carne")),
                    new ArrayList<>(),
                    80

            );
            CustomerDAO.editCliente(c);

            verify(pst).setString(1, "Maria");
            verify(pst).setString(2, "abc");
            verify(pst).setString(3, "M");
            verify(pst).setInt(4, 21);
            verify(pst).setString(8, "ID3");
        }
    }
}






