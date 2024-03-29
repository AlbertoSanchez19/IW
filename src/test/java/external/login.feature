Feature: login en servidor

#
#  Este test funciona, pero no es de buena educación martillear una API externa
#
#  Scenario: login malo en github
#    Given driver 'https://github.com/login'
#    And input('#login_field', 'dummy')
#    And input('#password', 'world')
#    When submit().click("input[name=commit]")
#    Then match html('.flash-error') contains 'Incorrect username or password.'
#

  Scenario: login malo en plantilla
    Given driver baseUrl + '/user/2'
    And input('#username', 'dummy')
    And input('#password', 'world')
    When submit().click("#do_login")
    #Then match html('.error') contains 'Error en nombre de usuario o contraseña'
    Then match driver.url contains 'error'


  @login_profesor
  Scenario: login correcto como profesor
    Given driver baseUrl + '/login'
    And input('#username', 'b')
    And input('#password', 'aa')
    When submit().click("#do_login")  
    Then waitForUrl(baseUrl + '/profesor')

  @login_admin
  Scenario: login correcto como a
    Given driver baseUrl + '/login'
    And input('#username', 'a')
    And input('#password', 'aa')
    When submit().click("#do_login")
    Then waitForUrl(baseUrl + '/admin')

  @login_alumno
  Scenario: login correcto como b
    Given driver baseUrl + '/login'
    And input('#username', 'b')
    And input('#password', 'aa')
    When submit().click("#do_login")  
    Then waitForUrl(baseUrl)

  Scenario: logout after login
    Given driver baseUrl + '/login'
    And input('#username', 'a')
    And input('#password', 'aa')
    When submit().click("#do_login")
    Then waitForUrl(baseUrl + '/admin')
    When submit().click("{button}logout")
    Then waitForUrl(baseUrl + '/login')
