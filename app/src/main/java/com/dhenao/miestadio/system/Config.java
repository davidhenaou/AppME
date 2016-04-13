package com.dhenao.miestadio.system;

import java.util.Date;

public class Config {

    //configuracion de perfil
    public static String UsuarioPerfil = "";
    public static String CorreoPerfil = "";
    public static String CelularPerfil = "";


    public static String ipequipo = "52.38.165.244";

    //direccion url donde subo las fotos
    public static final String FILE_UPLOAD_URL = "http://" + ipequipo + "/subirmultimedia.php";
    //directorio donde esta la multimedia
    public static final String IMAGE_DIRECTORY_NAME_LOCAL = "Subir_archivos";


    //directorio donde reviso los equipos que juegan
    public static final String URL_MYSQL_EQUIPOS = "http://" + ipequipo + "/mysql/obtener_equipos.php";
    public static final String URL_MYSQL_HORASYFECHASJUEGO = "http://" + ipequipo + "/mysql/obtener_informaciondejuego.php";
    public static final String URL_MYSQL_MINUTOAMINUTO = "http://" + ipequipo + "/mysql/obtener_minutoaminuto.php";
    public static final String URL_MYSQL_MINUTOSJUEGO = "http://" + ipequipo + "/mysql/obtener_minutos_juego.php";



    //informacion de minuto a minuto
    public static String pEquipo1NombreMaM = "";
    public static String pEquipo1DescripcionMaM = "";
    public static String pEquipo1ImagenMaM = "";

    public static String pEquipo2NombreMaM = "";
    public static String pEquipo2DescripcionMaM = "";
    public static String pEquipo2ImagenMaM = "";

    public static Date horapt;
    public static int  repopt;
    public static Date horast;
    public static int  repost;

    /*public static int minutosjuego = 0;
    public static int segundosjuego = 0;*/





}