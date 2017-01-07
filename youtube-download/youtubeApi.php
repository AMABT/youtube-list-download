<?php

include_once "config.php";

class YouTubeAPI {
    
    protected $cmd = "/usr/local/bin/youtube-dl --ffmpeg-location /usr/local/bin/ffmpeg";
    
    public function getUrlInfo($url) {
        
        $cmd = $this->cmd;
        $cmd .= " -J ".$url;
        
        $cmdResult = exec($cmd." 2>&1");
        $result = json_decode($cmdResult);
        
        if(!empty($result->_type) && $result->_type == 'playlist') {
            foreach($result->entries as &$val) {
                unset($val->formats);
            }
        } else {
            unset($result->formats);
        }
        
        return $result;
    }
    
    public function defaultResult($value = null) {
        return array('value' => $value);
    }
    
    public function downloadVideoAudio($link, $videoDownload = false) {
        
        $cmd = $this->cmd;
        
        if ($videoDownload) {
            $cmd .= " -f \"best[height<=900]\"";
        } else {
            $cmd .= " -x --audio-format \"".Config::$audioFormat."\"";
        }
        
        $cmd .= " -o \"".Config::$audioFolder.Config::$audioTitle."\" \"".$link."\"";
        
        $cmdResult = shell_exec($cmd." 2>&1");
        
        if ($videoDownload) {
            $titlePattern = '/[\\\\|\/](.*?)\.m4a/';
        } else {
            $titlePattern = '/'.str_replace('/','\/',Config::$audioFolder).'(.*?)\.mp3/';
        }
        
        preg_match_all($titlePattern, $cmdResult, $matches);
        
        if (empty($matches[1])) {
            return $this->defaultResult($cmdResult);
        }
        
        // get song/video name
        $fileTitle = $matches[1][0].'.';
        $fileTitle .= $videoDownload ? Config::$videoFormat : Config::$audioFormat;
        
        $file = Config::$audioFolder.$fileTitle;
        
        if (file_exists($file)) {
            header('Content-Description: File Transfer');
            header('Content-Type: application/octet-stream');
            header('Content-Disposition: attachment; filename="'.basename($file).'"');
            header('Expires: 0');
            header('Cache-Control: must-revalidate');
            header('Pragma: public ');
            header('Content-Length: '.filesize($file));
            readfile($file);
            exit;
        } else {
            return $this->defaultResult();
        }
    }
    
}