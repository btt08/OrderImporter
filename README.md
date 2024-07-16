# Kata: Importador de pedidos
## Objetivo
Importar a una base de datos, una lista de pedidos.
Para el nivel básico de dificultad, el origen de los pedidos serán ficheros CSV con una estructura fija

## Origen de los pedidos
Los pedidos a importar se obtendrán de un fichero CSV, con la siguiente estructura de columnas:

 - Order ID
 - Order Priority
 - Order Date
 - Region
 - Country
 - Item Type
 - Sales Channel
 - Ship Date
 - Units Sold
 - Unit Price
 - Unit Cost
 - Total Revenue
 - Total Cost
 - Total Profit

## Contexto:

Es un conjunto de pedidos que se han ido recopilando históricamente de manera manual, sin más revisión que la del humano que introducía los datos. Los datos de los pedidos a importar no son nada más que un volcado de los datos en papel, a un soporte digital, sin ningún tratamiento.

El importador se debe plantear de forma que el usuario le pase/indique cualquier fichero. Dejamos aquí dos ficheros a modo de ejemplo con datos de pedidos.

Descargar: [Fichero 1](files/RegistroVentas1.csv)  [Fichero 2](https://drive.google.com/file/d/1lLMqoS4dxaRM3NPFUsacq0Ca8_6RrygA/view?usp=sharing)

## Funcionalidad
 - Independientemente del origen de los pedidos, al terminar la importación, deberá mostrar un resumen del número de pedidos de cada tipo según distintas columnas.
 - Además deberá generar un fichero con los registros ordenados por número de pedido.
 - En el fichero sólo deberían aparecer los registros importados durante la lectura del fichero, sólo los de una importación.
 - El campo por el que tiene que estar ordenado el fichero resultante es orderId.
 - En el resumen final deberá salir el conteo por cada tipo de los campos: Region, Country, Item Type, Sales Channel, Order Priority.

## Formato del fichero exportado
El fichero resultante deberá ser un CSV, con las siguientes columnas:

 - Order ID
 - Order Priority
 - Order Date
 - Region
 - Country
 - Item Type
 - Sales Channel
 - Ship Date
 - Units Sold
 - Unit Price
 - Unit Cost
 - Total Revenue
 - Total Cost
 - Total Profit

Las fechas se tendrán que mostrar con el formato: "{día}/{mes}/{año}", siendo día y mes números de dos dígitos, y el año de cuatro. Ejemplo: 12/06/1986

## Requisitos
 - Lenguaje de programación: Java
 - Base de datos: cualquiera relacional con JDBC

## Cómo usar solución
 - Utilizar el [script sql](files/order.sql) para crear la tabla en MySQL
 - Modificar el archivo app.config con los valores correspondientes para DB_URL, DB_USER y DB_PWD
 - Ejecutar
