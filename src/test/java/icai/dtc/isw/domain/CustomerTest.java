package icai.dtc.isw.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    private Customer customer;
    private ArrayList<String> illegalFood;

    @BeforeEach
    void setUp() {
        illegalFood = new ArrayList<>(Arrays.asList("Gluten", "Lácteos"));
        customer = new Customer(
                "maria",
                "1234",
                "M",
                21,
                illegalFood,
                "Carne"
        );
    }
    //prueba generar id con formato correcto
    @Test
    void testCreateUserIdFormat() {
        String userId = Customer.createUserId("maria");
        assertTrue(userId.matches("[A-Z]\\d{5}"),
                "El userId debe empezar por una mayúscula y seguir con 5 dígitos");
    }
    //prueba el constructor y mira que los getters funcionen
    @Test
    void testConstructorAndGetters() {
        assertEquals("maria", customer.getUserName());
        assertEquals("1234", customer.getUserPass());
        assertEquals("M", customer.getUserGender());
        assertEquals(21, customer.getUserAge());
        assertEquals(illegalFood, customer.getIllegalFood());
        assertEquals("Carne", customer.getAlimentosNoCome());
        assertEquals("M00522", customer.getUserId(), "Para 'maria' el userId esperado es M00522");
    }
    //prueba que los setters funcionen
    @Test
    void testSetters() {
        customer.setUserId("A00001");
        customer.setUserPass("abcd");
        customer.setUserGender("M");
        customer.setUserAge(21);
        ArrayList<String> nuevaLista = new ArrayList<>(Arrays.asList("Pescado", "Marisco"));
        customer.setIllegalFood(nuevaLista);

        assertEquals("A00001", customer.getUserId());
        assertEquals("abcd", customer.getUserPass());
        assertEquals("M", customer.getUserGender());
        assertEquals(21, customer.getUserAge());
        assertEquals(nuevaLista, customer.getIllegalFood());
    }

    //prueba que se cree correctamente el id test para un mismo nombre
    @Test
    void testCreateUserId() {
        String id1 = Customer.createUserId("maria");
        String id2 = Customer.createUserId("maria");
        assertEquals(id1, id2, "El mismo nombre debería generar el mismo userId");
        assertEquals("M00522", id1);
    }

    //prueba que para dos nombres distintos se creen 2 id
    @Test
    void testDifferentIds() {
        String id1 = Customer.createUserId("maria"); // M00522
        String id2 = Customer.createUserId("juan");  // J00430
        assertNotEquals(id1, id2, "Nombres distintos deben generar IDs distintos");
        assertEquals("J00430", id2);
    }

    //prueba la lista illegalfood con diferentes estados
    @Test
    void illegalFoodToString() {
        // caso normal (concatena sin separador)
        assertEquals("GlutenLácteos", customer.illegalFoodToString());
        // lista vacía
        customer.setIllegalFood(new ArrayList<>());
        assertEquals("", customer.illegalFoodToString());
        // null => "e"
        customer.setIllegalFood(null);
        assertEquals("e", customer.illegalFoodToString());
    }
}

