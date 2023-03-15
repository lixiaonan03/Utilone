package com.lxn.utilone.activity

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alibaba.android.arouter.facade.annotation.Route
import com.lxn.utilone.R

@Route(path = ActivityConstans.COMPOSE_PATH, name = "compose学习页")
class CompressActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compress)

        val greeting = findViewById<ComposeView>(R.id.greeting)
        greeting.setContent {
            Greeting()
        }
        findViewById<TextView>(R.id.tvChange).setOnClickListener {
            imgStr += "+1"
            textStr = "文字改变的"
        }
    }


    var imgStr = "图片str"
    var textStr = "Alfred Sisley"


    @Preview
    @Composable
    private fun Greeting() {


        Column {
            Text(text =  "compose学习的", color = Color.Black)

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = textStr, color = Color.Red,

                    modifier = Modifier //在设置size之前设置padding相当于外边距
                        .padding(10.dp) //此时组件占据空间大小100.dp+外边距 即大小为110.dp*110.dp
                        //                    .size(width = 100.dp, height = 20.dp)
                        //在设置size之后设置相当于内边距，组件大小不变
                        //                    .padding(10.dp)
                        //设置背景,对应背景来说，在它之前设置的padding 就相当于外边距，所以背景的绘制大小只有90.dp*90.dp
                        //                    .background(Color.Transparent)
                        //                    .padding(20.dp)//内边距，背景大小不变
                        //添加点击事件，同理点击区域的大小90.dp-20.dp 所以可点击局域大小只有70.dp*70.dp
                        .clickable(onClick = {
                            textStr = "文字改变的"
                        })
                )
                Text("3 minutes ago")

            }
            Box() {
                Image(
                    painter = painterResource(id = R.drawable.adapter_cart_imgadd), contentDescription = imgStr, Modifier.padding(10.dp)
                )
            }
        }


    }
}