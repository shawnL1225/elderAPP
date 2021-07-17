<?php

$file_code = uniqid();
$DOCUMENT_ROOT = $_SERVER['DOCUMENT_ROOT'];
// $DOCUMENT_ROOT."/elderApp/headshot/profile".
$file_dir = './headshot/'.$file_code.".png";
$ifp = fopen( $file_dir, "wb" );
fwrite( $ifp, "1111111" ); 
fclose( $ifp ); 

echo $file_dir;

?>


<?php
// $filename = 'test.txt';
// $somecontent = "Add this to the file\n";

// // Let's make sure the file exists and is writable first.
// if (is_writable($filename)) {

//     // In our example we're opening $filename in append mode.
//     // The file pointer is at the bottom of the file hence
//     // that's where $somecontent will go when we fwrite() it.
//     if (!$handle = fopen($filename, 'wca')) {
//          echo "Cannot open file ($filename)";
//          exit;
//     }

//     // Write $somecontent to our opened file.
//     if (fwrite($handle, $somecontent) === FALSE) {
//         echo "Cannot write to file ($filename)";
//         exit;
//     }

//     echo "Success, wrote ($somecontent) to file ($filename)";

//     fclose($handle);

// } else {
//     echo "The file $filename is not writable";
// }
?>