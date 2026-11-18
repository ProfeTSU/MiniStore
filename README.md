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

## Estructura del proyecto
- `ministore.modelo`: clases del dominio (Categoria, Producto, Cliente, Venta...)
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
1. En MySQL Workbench, conectado como **root**, ejecutar en orden los scripts de la carpeta `sql`:
   1. `01_crear_bd_ministore.sql`: crea la base de datos `ministore` y sus 8 tablas.
   2. `02_datos_prueba_ministore.sql`: inserta los datos de prueba.
   3. `03_consultas_ministore.sql`: consultas de ejemplo (opcional).
2. En NetBeans, abrir `ministore/util/ConexionBD.java` y escribir en las constantes
   `USUARIO` y `CLAVE` el usuario y la contraseña de su propio MySQL.
3. Ejecutar `ministore/pruebas/PruebaConexion.java` con **Shift + F6**. Debe mostrar:
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
4. Por ahora, cada ventana se ejecuta por separado: abrir el archivo de la ventana
   (por ejemplo, `ministore/vista/VentanaProductos.java`) y presionar **Shift + F6**.

Si una ventana muestra datos viejos después de un cambio en el código, usar
**Clean and Build** (Shift + F11) y volver a ejecutarla.

## Estado del proyecto

| Vista | Estado |
|---|---|
| VentanaProductos | Conectada a MySQL |
| VentanaClientes | Conectada a MySQL |
| VentanaLogin, VentanaPrincipal | En construcción |
| VentanaVenta, DialogoComprobante | Pendientes |
|