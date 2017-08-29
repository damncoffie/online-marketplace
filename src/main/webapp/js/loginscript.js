/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
function checkUser() {
    $.ajax({url: "checkLogin?" + "login=" + $("#login").value + "&"
                + "password=" + $("#password").value,
        success: function (data) {
            return data;
        }
    });
}

