package io.mochadwi.util.binding

import android.util.Log
import android.webkit.WebView
import androidx.databinding.BindingAdapter
import io.mochadwi.util.ext.toJson
import kotlinx.serialization.internal.StringSerializer
import kotlinx.serialization.list

object WebBinding {

    @BindingAdapter("app:loadHtml", requireAll = false)
    @JvmStatic
    fun setWebContent(webView: WebView, data: String?) {
        webView.apply {
            data?.let {
                settings.apply {
                    defaultTextEncodingName = "utf-8"
                    useWideViewPort = true
                    loadWithOverviewMode = true
                    javaScriptEnabled = true
                    setInitialScale(1)
                }
                // array string: 0 = image; 1 = title; 2 = body
                data.split("#~!@#")
                        .filter { data -> !data.equals("null", true) && data != null && data != "null" }
                        .takeIf { data -> data.isNotEmpty() }
                        ?.let { htmlData ->
                            Log.d("WEB-CONTENT", htmlData.toJson(StringSerializer.list))
                            var htmlStyle = """
                        |<head>
                        |<meta name="viewport" content="width=device-width, initial-scale=1">
                        |   <style>
                        |       .responsive {
                        |            width: 100%;
                        |           height: auto;
                        |           }
                        |        .video-responsive{
                        |            overflow:hidden;
                        |            padding-bottom:56.25%;
                        |            position:relative;
                        |            height:0;
                        |        }
                        |        .video-responsive iframe{
                        |            left:0;
                        |            top:0;
                        |            height:100%;
                        |            width:100%;
                        |            position:absolute;
                        |        }
                        |   </style>
                        |</head>
                        |<body>
                        |<object width=100% type="image/png">
                        |   <img border="0" width=100% src="${htmlData[0]}" alt="${htmlData[1]}" />
                        |</object><br><br>
                    """.trimMargin()
                            // TODO: Change this to use html files instead, with append method
                            // REF: http://androidtrainningcenter.blogspot.com/2012/11/android-webview-loading-custom-html-and.html
                            // REF: https://www.mkyong.com/java/how-to-append-content-to-file-in-java/
                            // REF: http://www.java67.com/2015/07/how-to-append-text-to-existing-file-in-java-example.html
                            // REF: https://stackoverflow.com/questions/5749569/load-html-file-into-webview
                            var htmlResponsive = "$htmlStyle${if (htmlData[2].isNullOrBlank()) "No messages" else htmlData[2]}"
                            htmlResponsive = htmlResponsive.replace("""style="width: 1000px;"""", """class="responsive""""") // responsive image
                            htmlResponsive = htmlResponsive.replace("""<iframe src=""", """<div class="video-responsive"><iframe src=""") // responsive video
                            htmlResponsive = htmlResponsive.replace("""</iframe>""", """</iframe></div>""") // responsive video

                            loadDataWithBaseURL(
                                    "file:///android_res/mipmap/",
                                    // data is assigned from mipmap, ref: https://stackoverflow.com/a/4534886/3763032
                                    """
                                |$htmlResponsive
                                |</body>
                            """.trimMargin(),
                                    "text/html; charset=utf-8",
                                    "utf-8",
                                    "")
                        }
            }
        }

    }
}