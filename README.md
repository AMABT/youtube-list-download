# youtube-list-download

Android application to download Youtube url (playlist/song) as mp3

Setup:
- Install apache and copy youtube-download/ server to it
- This server is just an API to youtube-dl https://github.com/rg3/youtube-dl
    - For Mac: brew install youtube-dl & brew install ffmpeg
    - For Linux/Windows: pip install --upgrade youtube-dl & pip install --upgrade ffmpeg
- Change the url to server in resources/strings.xml
- Install app on phone