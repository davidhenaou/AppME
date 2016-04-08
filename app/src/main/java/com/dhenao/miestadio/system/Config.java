package com.dhenao.miestadio.system;

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


}