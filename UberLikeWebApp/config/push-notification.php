<?php

return array(

    'IOSUser'     => array(
        'environment' => env('IOS_USER_ENV', 'development'),
        'certificate' => app_path().'/apns/user/tranxit_user_dev.pem',
        'passPhrase'  => env('IOS_USER_PUSH_PASS', 'appoets123$'),
        'service'     => 'apns'
    ),
    'IOSProvider' => array(
        'environment' => env('IOS_PROVIDER_ENV', 'development'),
        'certificate' => app_path().'/apns/provider/tranxit_provider_dev.pem',
        'passPhrase'  => env('IOS_PROVIDER_PUSH_PASS', 'appoets123$'),
        'service'     => 'apns'
    ),
    'AndroidUser' => array(
        'environment' => env('ANDROID_USER_ENV', 'production'),
        'apiKey'      => env('ANDROID_USER_PUSH_KEY', 'yourAPIKey'),
        'service'     => 'gcm'
    ),
    'AndroidProvider' => array(
        'environment' => env('ANDROID_PROVIDER_ENV', 'production'),
        'apiKey'      => env('ANDROID_PROVIDER_PUSH_KEY', 'yourAPIKey'),
        'service'     => 'gcm'
    )

);