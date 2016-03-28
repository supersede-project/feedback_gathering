var webdriver = require('selenium-webdriver');
var server = require('node-http-server');
var path = require('path');
var chai = require('chai');
var chaiDriver = require('chai-webdriver');
var test = require('selenium-webdriver/testing');

var describe = test.describe;
var it = test.it;
var expect = chai.expect;

var driver = new webdriver.Builder().
    withCapabilities(webdriver.Capabilities.chrome()).
    build();

chai.use(chaiDriver(driver));

server.deploy(
    {
        port: 8000,
        root: path.resolve('./')
    }
);

describe('test suite for feedback jQuery plugin', function() {
    it('should display the options', function() {
        driver.get('http://localhost:8000');
    });
});
