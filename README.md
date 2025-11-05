# ISW-Grupo3
Descripción:
Esta aplicación genera menús semanales personalizados adaptados al perfil y presupuesto del usuario.
Tiene en cuenta datos como la edad, el sexo, las intolerancias alimentarias, los alimentos no consumidos y el presupuesto máximo semanal, para crear menús equilibrados y económicos de lunes a viernes.
Además, ofrece una lista de la compra asociada y permite cambiar recetas por otras similares según las preferencias del usuario.

Funcionalidades principales:
-Registro y gestión del perfil de usuario.
-Generación automática de menús personalizados.
-Cálculo del precio total según los ingredientes y el supermercado seleccionado.
-Creación de la lista de la compra.
-Sustitución de recetas por opciones similares.

Estructura del dominio:
-Customer: representa al usuario y sus datos personales y alimentarios.
-Menu: agrupa dos recetas (comida y cena) y controla el presupuesto.
-Receta: contiene la información de cada plato (ingredientes, dificultad, duración y precio).
-Ingrediente: define los componentes de cada receta y está asociado a un producto.
-Producto: representa el alimento real con su nombre, precio, categoría y supermercado.
-Unidad: define las unidades de medida utilizadas por los ingredientes.
-Lista: genera la lista de la compra derivada de los menús.
-Dificultad: enumera los niveles de elaboración de las recetas.

Objetivo: Facilitar la organización de la alimentación semanal, optimizando tiempo, dinero y esfuerzo, mediante menús variados y adaptados a las necesidades de cada usuario.