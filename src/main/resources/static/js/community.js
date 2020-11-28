function post() {
    var questionId = $("#question_id").val();
    var content = $("#comment_content").val();
    comment2target(questionId, 1, content);
}

function comment2target(targetId, type, content) {

    if (!content) {
        alert("评论内容不能为空~~~");
        return;
    }

    $.ajax({
        type: "POST",
        url: "/comment",
        contentType: "application/json",
        data: JSON.stringify({
            "parentId":targetId,
            "content":content,
            "type":type
        }),
        success: function (response) {
            if (response.code == 200) {
                window.location.reload();
            }
            else {
                if (response.code==2000) {
                    var isAccepted = confirm(response.message);
                    if (isAccepted) {
                        window.open("https://github.com/login/oauth/authorize?client_id=b0d8aca9afe1c249ac53&redirect_uri=http://localhost:8887/callback&scope=user&state=1");
                        window.localStorage.setItem("closable", true);
                    }
                }
                alert(response.message);
            }
            console.log(response);
        },
        dataType: "json"
    });
}


function comment(e) {
    var commentId = e.getAttribute("data-id");
    var content = $("#input-"+commentId).val();
    comment2target(commentId, 2, content);
}

function collapseComments(e) {
    var id = e.getAttribute("data-id");
    var comments = $("#comment-"+id);
    var icon = $("#icon-"+id);

    //获取二级评论的展开状态
    var collapse = e.getAttribute("data-collapse");
    if (collapse) {
        //折叠
        comments.removeClass("in");
        icon.removeClass("icon-open");
        e.removeAttribute("data-collapse");
    }
    else {
        //展开
        var subCommentContainer = $("#comment-"+id);
        if (subCommentContainer.children().length <= 1) {

            $.getJSON("/comment/" + id, function (data) {
               console.log(data);
               $.each(data.data.reverse(), function (index, comment) {

                    var mediaLeft = $("<div/>", {
                        "class":"media-left"
                    }).append($("<img/>", {
                          "class":"media-object img-rounded",
                          "src":comment.user.avatarUrl
                      }));

                    var mediaBody = $("<div/>", {
                        "class":"media-body"
                    }).append($("<h5/>", {
                        "class":"media-heading",
                        "html":comment.user.name
                    })).append($("<div/>", {
                        "class":"menu"
                    })).append($("<div/>", {
                        "html":comment.content
                    })).append($("<span/>", {
                        "class":"pull-right",
                        "html":moment(comment.gmtCreate).format("YYYY-MM-DD")
                    }));

                    var mediaElement = $("<div/>", {
                        "class":"media"
                    }).append(mediaLeft)
                        .append(mediaBody);

                    var commentElement = $("<div/>", {
                        "class": "col-lg-12 col-md-12 col-sm-12 col-xs-12 comments"
                    }).append(mediaElement);
                    subCommentContainer.prepend(commentElement);
               });
            });
       }
       comments.addClass("in");
       icon.addClass("icon-open");
       e.setAttribute("data-collapse", "in");
    }
}