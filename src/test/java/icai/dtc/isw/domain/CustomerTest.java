package icai.dtc.isw.domain;

import icai.dtc.isw.utils.Util;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    private Customer customer;
    private ArrayList<String> illegalFood;
    private ArrayList<String> noCome;
    private Util util;

    @BeforeEach
    void setUp() {
        util = new Util();
        illegalFood = new ArrayList<>(Arrays.asList("Gluten", "Lacteos"));
        noCome = new ArrayList<>(Arrays.asList("Carne"));

        customer = new Customer(
                "maria",
                "1234",
                "M",
                21,
                illegalFood,
                noCome
        );
    }

    //prueba que el id generado cumple el formato esperado
    @Test
    void testCreateUserIdFormat() {
        String userId = util.createUserId("maria");
        assertTrue(userId.matches("[A-Z]\\d{5}"));
    }

    //prueba el constructor y los getters
    @Test
    void testConstructorYGetters() {
        assertEquals("maria", customer.getUserName());
        assertEquals("1234", customer.getUserPass());
        assertEquals("M", customer.getUserGender());
        assertEquals(21, customer.getUserAge());
        assertEquals(illegalFood, customer.getIllegalFood());
        assertEquals(noCome, customer.getAlimentosNoCome());

    }
    //prueba los setters
    @Test
    void testSetters() {
        ArrayList<String> nuevaLista = new ArrayList<>(Arrays.asList("Pescado", "Marisco"));
        ArrayList<String> nuevoNoCome = new ArrayList<>(Arrays.asList("Brocoli"));

        customer.setUserId("A00001");
        customer.setUserPass("abcd");
        customer.setUserGender("F");
        customer.setUserAge(30);
        customer.setIllegalFood(nuevaLista);
        customer.setAlimentosNoCome(nuevoNoCome);

        assertEquals("A00001", customer.getUserId());
        assertEquals("abcd", customer.getUserPass());
        assertEquals("F", customer.getUserGender());
        assertEquals(30, customer.getUserAge());
        assertEquals(nuevaLista, customer.getIllegalFood());
        assertEquals(nuevoNoCome, customer.getAlimentosNoCome());
    }

    //prueba que el mismo nombre genere el mismo id
    @Test
    void testCreateUserId() {
        String id1 = util.createUserId("maria");
        String id2 = util.createUserId("maria");
        assertEquals(id1, id2);
    }

    //prueba que dos nombres distintos generen ids distintos
    @Test
    void testDifferentIds() {
        String id1 = util.createUserId("maria");
        String id2 = util.createUserId("juan");
        assertNotEquals(id1, id2);
    }

    //prueba que se devuelva una cadena vacia cuando no hay lista
    @Test
    void testIllegalFoodToString() {
        assertEquals("Gluten,Lacteos", customer.illegalFoodToString());

        customer.setIllegalFood(new ArrayList<>());
        assertEquals("", customer.illegalFoodToString());

        customer.setIllegalFood(null);
        assertEquals("", customer.illegalFoodToString());
    }

    //prueba si un alimento está en las restricciones o preferencias del usuario.
    @Test
    void testNoPuedeConsumirNombre() {
        assertTrue(customer.noPuedeConsumirNombre("Pan con gluten"));
        assertTrue(customer.noPuedeConsumirNombre("carne de cerdo"));
        assertFalse(customer.noPuedeConsumirNombre("Ensalada de tomate"));
    }

    //prueba que funciona correctamente en función del nombre del ingrediente o producto.
    @Test
    void testNoPuedeConsumirIngredienteYProducto() {
        Ingrediente ingOk = new Ingrediente("tomate", "100g");
        Ingrediente ingMal = new Ingrediente("harina de gluten", "100g");
        Ingrediente ingNoCome = new Ingrediente("carne picada", "200g");

        assertFalse(customer.noPuedeConsumir(ingOk));
        assertTrue(customer.noPuedeConsumir(ingMal));
        assertTrue(customer.noPuedeConsumir(ingNoCome));

        Producto prodOk = new Producto("manzana", Unidad.u, 1.0);
        Producto prodMal = new Producto("pan con gluten", Unidad.u, 1.0);


        assertFalse(customer.noPuedeConsumir(prodOk));
        assertTrue(customer.noPuedeConsumir(prodMal));
    }
}



