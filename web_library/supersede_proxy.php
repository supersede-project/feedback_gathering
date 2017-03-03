<?php
    $jsonurl = "https://supersede-develop.atosresearch.eu:8443/". $_GET['url'];
    //$jsonurl = "http://supersede.es.atos.net:8280/" . $_GET['url'];

    $ch = curl_init();
    curl_setopt($ch, CURLOPT_URL, $jsonurl);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true );
    curl_setopt($ch, CURLOPT_CONNECTTIMEOUT, 7);
    curl_setopt($ch, CURLOPT_TIMEOUT, 7);

    if(isset($_GET['method']) && $_GET['method'] == 'POST') {
        curl_setopt($ch, CURLOPT_POST, true);

    }


    $result = curl_exec($ch);

    if( !$result ) {
	    die('Accessing: ' . $jsonurl . '. Error: "' . curl_error($ch) . '" - Code: ' . curl_errno($ch));
    }
    curl_close($ch);
    echo $result;
?>
