<?php

    function base64_to_jpeg( $base64, $output_file ) {
        $ifp = fopen( $output_file, "wb" ); 
        fwrite( $ifp, base64_decode( $base64) ); 
        fclose( $ifp ); 
        return( $output_file ); 
    }

    if(isset($_POST['imageBase64'])){
        $base64_string = $_POST['imageBase64'];
        $file_code = uniqid();
        $file_dir = "./headshot/profile".$file_code;
        $image = base64_to_jpeg($base64_string, $file_dir);
        
        echo $file_code;
    }

    
    
    
?>