function fn() {
    var env = karate.env; // get system property 'karate.env'
    karate.log('karate.env system property was:', env);
    

    if (!env) {
        env = 'dev';
    }

    /**
     * Variables here are available in all tests
     */
    var config = {
        env: env,
        myVarName: 'someValue',
        baseUrl: 'http://localhost:7070'
    }

    /**
     * Drivers for tests - currently configured value is good for Linux
     */
    addOptions: ['--remote-allow-origins=*', '--disable-notifications' ], 
    karate.configure('driver', {
        type: 'chrome',
        // descomentar para chromium bajo linux
        // executable: '/usr/bin/chromium-browser',
        addOptions: ['--remote-allow-origins=*', '--disable-notifications' ], 
        executable: "C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe",
        showDriverLog: true,
        port: 8080
    })

    if (env == 'dev') {
        // customize
        // e.g. config.foo = 'bar';
    } else if (env == 'e2e') {
        // customize
    }
    return config;
}