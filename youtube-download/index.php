<?php

require_once "youtubeApi.php";

// youtube-dl -x --audio-format mp3 -o Mp3/%(title)s.%(ext)s https://www.youtube.com/watch?v=76RbWuFll0Y

$data = json_decode(file_get_contents('php://input'));
// $data = array(
//     'action' => 'download',
//     'url' => 'https://www.youtube.com/playlist?list=PLkHWMo6SQrJDyB17BBFj29BhjPm82JDnC'
// );
$ytApi = new YouTubeAPI();
$result = null;

switch($data->action) {
    case 'download':
        // in case of successful download, the function will write the binary output followed by exit();
        $result = $ytApi->downloadVideoAudio($data->url);
        break;
    case 'getInfo':
        $result = $ytApi->getUrlInfo($data->url);
        break;
    default:
        $result = $ytApi->defaultResult();
}

//$result = $ytApi->getUrlInfo($data['url']);
//echo '<pre>'; print_r($result);
echo json_encode($result);