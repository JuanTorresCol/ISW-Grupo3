package icai.dtc.isw.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;

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

    @Test
    void testCreateUserIdFormat() {
        String userId = Customer.createUserId("maria");
        assertTrue(userId.matches("[A-Z]\\d{5}"),
                "El userId debe empezar por una mayúscula y seguir con 5 dígitos");
    }

    @Test
    void testConstructorAndGetters() {
        assertEquals("maria", customer.getUserName());
        assertEquals("1234", customer.getUserPass());
        assertEquals("M", customer.getUserGender());
        assertEquals(21, customer.getUserAge());
        assertEquals(illegalFood, customer.getIllegalFood());
        assertEquals("Carne", customer.getAlimentosNoCome());
        assertNotNull(customer.getUserId(), "El userId no debe ser nulo");
    }

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

    @Test
    void testCreateUserIdConsistency() {
        String id1 = Customer.createUserId("maria");
        String id2 = Customer.createUserId("maria");
        assertEquals(id1, id2, "El mismo nombre debería generar el mismo userId");
    }

    @Test
    void testDifferentNamesProduceDifferentIds() {
        String id1 = Customer.createUserId("maria");
        String id2 = Customer.createUserId("juan");
        assertNotEquals(id1, id2, "Nombres distintos deben generar IDs distintos");
    }
}
