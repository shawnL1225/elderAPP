<?php

    function base64_to_jpg( $base64, $output_file ) {
        $ifp = fopen( $output_file, "wb" ); 
        fwrite( $ifp, base64_decode( $base64) ); 
        fclose( $ifp ); 
    }

    if(isset($_POST['imageBase64'])){
        $base64_string = $_POST['imageBase64'];
        $file_code = uniqid();
        $file_dir = "./headshot/".$file_code.".jpg";
        base64_to_jpg($base64_string, $file_dir);
        
        if(file_exists($file_dir))  echo $file_code;
    }

    
    
    
?>