package huaan.com.statisticsdemo

import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import huaan.com.statisticsdemo.widget.CircularRingView
import huaan.com.statisticsdemo.widget.dp2px
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
        val datas = mutableListOf(
                CircularRingView.CircularRingElement(Color.parseColor("#1afa29"), 433f),
                CircularRingView.CircularRingElement(Color.parseColor("#b5a642"), 52f),
                CircularRingView.CircularRingElement(Color.parseColor("#d9d919"), 32f),
                CircularRingView.CircularRingElement(Color.parseColor("#ff0000"), 21f)
        )
        crv_attendance.setElementList(datas)
        crv_attendance.setRingThickness(crv_attendance.dp2px(20f))
        tv_attendance_report_sum.text = String.format("总数/%d人",536)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
