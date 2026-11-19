# MiniStore - Sistema de Gestión de Ventas

Proyecto Integrador - Programación I (Java, POO)
Tecnología en Sistemas de Información - UNIAJC

## Descripción
Aplicación de escritorio para la gestión básica de ventas de una pequeña tienda:
productos, clientes, ventas e inventario. Los datos se guardan en una base de
datos **MySQL**.

## Arquitectura
El proyecto sigue el patrón **MVC** con repositorios (DAO):

```
Vista (Swing)  →  Controlador  →  Repositorio  →  MySQL
 lee y muestra      valida          guarda (SQL)
```

La clase `Aplicacion` arma el programa: crea los repositorios una sola vez
(para que todas las ventanas compartan los mismos) y abre cada ventana con su
controlador.

```
MiniStore (main)  →  Aplicacion  →  VentanaLogin  →  VentanaPrincipal  →  Productos, Clientes...
```

## Estructura del proyecto
- `ministore`: `MiniStore` (el main del proyecto) y `Aplicacion` (arma el programa)
- `ministore.modelo`: clases del dominio (Categoria, Producto, Cliente, Usuario, Venta...)
- `ministore.repositorio`: acceso a la base de datos MySQL (los DAO)
- `ministore.controlador`: validaciones y lógica entre la vista y los repositorios
- `ministore.vista`: interfaz gráfica (Swing)
- `ministore.util`: utilidades (`ConexionBD`, `Seguridad`)
- `ministore.pruebas`: clases de prueba, una por cada clase del proyecto
- `lib/`: conector de MySQL para Java (`mysql-connector-j`)

## Requisitos
- JDK 25
- Apache NetBeans 29
- MySQL Server 8 y MySQL Workbench
- El conector `mysql-connector-j` (ya incluido en la carpeta `lib`)

## Preparar la base de datos
1. Descargar los scripts SQL de la carpeta compartida por el docente (Drive).
2. En MySQL Workbench, conectado como **root**, ejecutarlos en orden:
   1. `01_crear_bd_ministore.sql`: crea la base de datos `ministore` y sus 8 tablas.
   2. `02_datos_prueba_ministore.sql`: inserta los datos de prueba.
   3. `03_consultas_ministore.sql`: consultas de ejemplo (opcional).
3. En NetBeans, abrir `ministore/util/ConexionBD.java` y escribir en las constantes
   `USUARIO` y `CLAVE` el usuario y la contraseña de su propio MySQL.
4. Ejecutar `ministore/pruebas/PruebaConexion.java` con **Shift + F6**. Debe mostrar:
   ```
   ¡Conexión exitosa!
   Base de datos: ministore
   Productos activos en MySQL: 16
   ```

Si una prueba cambia los datos, se vuelve al estado inicial ejecutando otra vez el script 02.

## Cómo ejecutarlo
1. Clonar el repositorio o descargarlo como ZIP.
2. Abrirlo en NetBeans: **File → Open Project**.
3. Preparar la base de datos (sección anterior).
4. Ejecutar el proyecto con **F6** (Run Project): abre el inicio de sesión.

Usuarios de prueba (la contraseña es el usuario + `123`):

| Usuario | Contraseña | Rol |
|---|---|---|
| admin | admin123 | Administrador |
| carlos | carlos123 | Vendedor |
| camilo | camilo123 | Vendedor desactivado (no puede entrar) |

Cada ventana también se puede probar sola: abrir su archivo y presionar **Shift + F6**.

Si una ventana muestra datos viejos después de un cambio en el código, usar
**Clean and Build** (Shift + F11) y volver a ejecutarla.

## Estado del proyecto

| Vista | Estado |
|---|---|
| VentanaLogin | Conectada a MySQL |
| VentanaPrincipal | Conectada (el menú cambia según el rol) |
| VentanaProductos | Conectada a MySQL |
| VentanaClientes | Conectada a MySQL |
| VentanaVenta, DialogoComprobante | Pendientes |
| VentanaUsuarios, VentanaReportes, VentanaConfiguracion | Pendientes |

## Versiones (etiquetas de Git)
- `v4-memoria`: última versión con los datos en memoria (ArrayList), antes de MySQL.

## Autor
Mauricio A. Ledesma - Docente Programación I