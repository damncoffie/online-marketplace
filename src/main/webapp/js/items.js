$(document).ready(function () {
    $("#table").tablesorter();
    //refreshOnLoad();
    //setInterval(refreshOnAjax, 1000);
}
);

// метод, который аджаксом обновляет таблицу через интервалы времени
function refreshOnAjax() {
    // определяем параметр, который отправится в реквесте к сервлету
    // параметр type проставляется сервлетами и определяется на странице jsp..
    // .. в самом первом подключенном скрипте (т.е. до загрузки *этого* скрипта)
    var mydata;
    // все товары (для гостя)
    if (type === "all") {
        mydata = {
            type: type
        };
    }
    // все товары, кроме моих (для зареганного)
    if (type === "others") {
        mydata = {
            type: type,
            id: id
        }
    }
    // поиск товаров
    if (type === "search") {
        mydata = {
            type: type,
            searchkey: searchkey,
            searchtype: searchtype
        }
    }

    // отрпавляем запрос
    $.ajax({url: "/Auction/serv/ajax",
        type: 'GET',
        data: mydata,
        datatype: "application/json",
        error: function (jqXHR, textStatus, errorThrown) {
            console.log("Ajax error: " + textStatus + ":" + errorThrown);
        },
        // (в ответе - объект с двумя массивами - товары и ставки)
        success: function (data) {
            test(data);
        }
    });
}

var currentItems = [];
function test(data) {
    var items = data[0];
    var bids = data[1];

    // если есть данные
    if (items.length !== 0) {
        // проверка, если это не первая итерация, и в массив уже что-то добавлялось
        if (currentItems.length === items.length) {
            console.log("currnet items list is equals to new");
        } else {
            if (currentItems.length !== 0) {
                var onlyInA = items.filter(comparer(currentItems));
                var onlyInB = currentItems.filter(comparer(items));
                // разница между последним массивом товаров и новым
                itemsDiffRes = onlyInA.concat(onlyInB);
                // если есть разница, дорисовываем строки для новых товаров
                if (itemsDiffRes.length !== 0) {
                    console.log("New items: " + items.length);
                    console.log("Old items: " + currentItems.length);
                    console.log("Diff: " + itemsDiffRes.length);
                    createNewRows(itemsDiffRes, bids);
                } else {
                    console.log("no diff");
                }
            } else {
                console.log("current items list is empty");
            }
        }
        // обнуляем текущий массив товаров
        currentItems = [];

        $.each(items, function (i, item) {
            // заполняем массив товарами (всегда текущими)
            currentItems.push(item);
            var dateDiff = getDiffAjax(item);
            $("#sellerid" + item.itemID).empty().append(item.sellerID + "old ajax");
            $("#title" + item.itemID).empty().append(item.title);
            $("#description" + item.itemID).empty().append(item.description);
            $("#startprice" + item.itemID).empty().append(item.startPrice);
            $("#bidincrement" + item.itemID).empty().append(item.bidIncrement);
            $("#datediff" + item.itemID).empty().append(dateDiff);

            if (item.isSold === 0) {
                if (bids[i] !== null) {
                    $("#bid" + item.itemID).empty().append(bids[i].bid);
                    $("#bidder" + item.itemID).empty().append(bids[i].bidder_id);
                    $("#bidinput" + item.itemID).attr("min", bids[i].bid + item.bidIncrement);
                } else {
                    $("#bidinput" + item.itemID).attr("min", item.startPrice + item.bidIncrement);
                    console.log("ok");
                    console.log(item.startPrice + item.bidIncrement);
                }
            }

            if (item.isSold === 1) {
                $("#bidbuy" + item.itemID).empty().append("item is sold after");
            }
        });
    } else {
        console.log("there is no items");
    }
}

// создаем строки для новых товаров
function createNewRows(items, bids) {
    console.log("new row for new item added");
    $.each(items, function (i, item) {
        // смотрим, что с датой (может быть просрочена)
        var dateDiff = getDiffAjax(item);
        var tr = "<tr>" +
                "<td" + " id=\"sellerid" + item.itemID + "\">" + item.sellerID + "neww ajax" + "</td>" +
                "<td" + " id=\"title" + item.itemID + "\">" + item.title + "</td>";
        if (item.description === undefined) {
            tr += "<td" + " id=\"description" + item.itemID + "\">" + "</td>";
        } else {
            tr += "<td" + " id=\"description" + item.itemID + "\">" + item.description + "</td>";
        }
        tr += "<td" + " id=\"startprice" + item.itemID + "\">" + item.startPrice + "</td>" +
                "<td" + " id=\"bidincrement" + item.itemID + "\">" + item.bidIncrement + "</td>" +
                "<td" + " id=\"datediff" + item.itemID + "\">" + dateDiff + "</td>";
        // old
        if (bids[i] === null) {
            tr += "<td" + " id=\"bid" + item.itemID + "\">" + "</td>" +
                    "<td" + " id=\"bidder" + item.itemID + "\">" + "</td>";
        } else {
            tr += "<td" + " id=\"bid" + item.itemID + "\">" + bids[i].bid + "</td>"
                    + "<td" + " id=\"bidder" + item.itemID + "\">" + bids[i].bidder_id + "</td>";
        }
        // если не гость
        if (login !== "guest") {
            // если товар не продан
            if (item.isSold === 0) {
                if (item.buyItNow === 0) {
                    if (bids[i] !== null) {
                        tr += "<td" + " id=\"bidbuy" + item.itemID + "\">" + "<form action=\"/Auction/serv/bid\" method=\"POST\">" +
                                "<input" + "" + "class=\"bid\" name=\"bid\" type=\"number\" step=\"" + item.bidIncrement + "\" " +
                                "min=\"" + (bids[i].bid + item.bidIncrement) + "\" required=\"requierd\" onchange=\"return checkMinus()\"/>" +
                                "<input type=\"hidden\" name=\"itemid\" value=\"" + item.itemID + "\"/><input type=\"submit\" value=\"Bid\"/></form>" + "</td>";
                    } else {
                        tr += "<td" + " id=\"bidbuy" + item.itemID + "\">" + "<form action=\"/Auction/serv/bid\" method=\"POST\">" +
                                "<input class=\"bid\" name=\"bid\" type=\"number\" step=\"" + item.bidIncrement + "\" " +
                                "min=\"" + (item.bidIncrement) + "\" required=\"requierd\" onchange=\"return checkMinus()\"/>" +
                                "<input type=\"hidden\" name=\"itemid\" value=\"" + item.itemID + "\"/><input type=\"submit\" value=\"Bid\"/></form>" + "</td>";
                    }
                }
                if (item.buyItNow === 1) {
                    tr += "<td" + " id=\"bidbuy" + item.itemID + "\">" + "<form action=\"/Auction/serv/buy\" method=\"POST\">" +
                            "<input type=\"hidden\" name=\"itemid\" value=\"" + item.itemID + "\"/>" +
                            "<input type=\"submit\" value=\"Buy now!\"/> </form>" + "</td>" + "</tr>";
                }
            }
            // если товар продан
            if (item.isSold === 1) {
                tr += "<td" + " id=\"bidbuy" + item.itemID + "\">" + "item is sold" + "</td>" + "</tr>";
            }
        } else { // если гость
            if (item.isSold === 1) {
                tr += "<td" + " id=\"bidbuy" + item.itemID + "\">" + "item is sold" + "</td>" + "</tr>";
            } else {
                tr += "<td" + " id=\"bidbuy" + item.itemID + "\">" + "</td>" + "</tr>";
            }
        }
        // end old
        $("#tablebody").append(tr);
    }
    );
}

// чекаем дату в очередной итерации аджакс-обновления
// (смотрим на данные из json'а)
function getDiffAjax(item) {

// 1999-07-07T23:59:59 - моя условная дата. обозначает..
// что товар является buy-it-know
    if (item.buyItNow === 1 || item.finishDate === "1999-07-07T23:59:59") {
        return "buy-it-now item after";
    }
    if (item.isSold === 1) {
        $("#bidbuy" + item.itemID).empty().append("item is sold after");
        return "item is sold after";
    }

    var x = formatTime(item.finishDate);
    if (x === "expired" && item.isSold === 0) {
        $("#bidbuy" + item.itemID).empty().append("item is sold after");
        sellItem({
            itemid: item.itemID,
            nondispatch: "yes"
        });
        return "item is sold after";
    }

    return formatTime(item.finishDate);
}

// метод, который проверяет данные при первой загрузке старницы
// (смотрим поля, которые проставила jsp)
function refreshOnLoad() {
    var dateElements = document.getElementsByClassName("datediff");
    for (var i = 0; i < dateElements.length; i++) {
        var element = dateElements[i];
        var elementDate = element.innerText;
        var formatedDate = getDiffOnLoad(elementDate);
        if (formatedDate === "expired") {
            element.innerText = "item is sold before(exp!!)";
            var itemID = element.id.replace("datediff", "");
            element.nextElementSibling.nextElementSibling.nextElementSibling.innerText = "item is sold before(exp)";
            sellItem({
                itemid: itemID,
                nondispatch: "yes"
            });
            continue;
        }
        element.innerText = formatedDate;
    }
}

// чекаем дату при первой загрузке страницы
// (смотрим поля, которые проставила jsp)
function getDiffOnLoad(date) {

    if (date === "buy-it-now item") {
        return "buy-it-now item before";
    }
    if (date === "item is sold") {
        return "item is sold before";
    }

    return formatTime(date);
}

// форматируем дату под DD:MM:HH:SS
function formatTime(date) {
    var then = new Date(date);
    var now = new Date();
    if ((then - now) <= 0) {
        return "expired";
    }
// количество секунд
    var delta = Math.abs(then - now) / 1000;
    // считаем(и вычитаем) дни
    var days = Math.floor(delta / 86400);
    delta -= days * 86400;
    // считаем(и вычитаем) часы
    var hours = Math.floor(delta / 3600) % 24;
    delta -= hours * 3600;
    // считаем(и вычитаем) минуты
    var minutes = Math.floor(delta / 60) % 60;
    delta -= minutes * 60;
    // остаток - секунды
    var seconds = Math.floor(delta % 60);
    return days + "d:" + hours + "h:" + minutes + "m:" + seconds + "s";
}

// ajax-запрос с целью продать(обновить в базе isSold).
// от сервлета ответа не ждем
function sellItem(data) {
    $.ajax({
        url: "/Auction/serv/buy",
        type: 'POST',
        success: function (data, textStatus, jqXHR) {
            console.log("sold by ajax");
        },
        data: data
    });
}

// если кто-то вздумает сделать отрицательную ставку
function checkMinus() {
    var bid = $("bid").value;
    if (bid < 0) {
        return false;
    }
    return true;
}

function comparer(otherArray) {
    return function (current) {
        return otherArray.filter(function (other) {
            return other.itemID === current.itemID;
        }).length === 0;
    };
}
