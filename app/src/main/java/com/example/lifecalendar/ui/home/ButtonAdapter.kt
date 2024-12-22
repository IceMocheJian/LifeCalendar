import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.lifecalendar.R

class ButtonAdapter(private var items: MutableList<String>) : RecyclerView.Adapter<ButtonAdapter.ButtonViewHolder>() {

    private var weeks: Int = 0 // 添加一个字段来存储周数

    class ButtonViewHolder(val button: Button) : RecyclerView.ViewHolder(button)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ButtonViewHolder {
        val button = Button(parent.context).apply {
            background = parent.context.getDrawable(R.drawable.rounded_button) // 使用圆角背景

            val params = ViewGroup.MarginLayoutParams(
//                 ViewGroup.LayoutParams.MATCH_PARENT,
//                 ViewGroup.LayoutParams.WRAP_CONTENT
                140,
                140
            ).apply {
                setMargins(10, 10, 10, 10) // 设置每个按钮的上下左右边距
            }
            layoutParams = params

        }

        return ButtonViewHolder(button)
    }



    override fun onBindViewHolder(holder: ButtonViewHolder, position: Int) {
        val params = holder.button.layoutParams as ViewGroup.MarginLayoutParams
        if (position % 48 in 0..5) {
            params.setMargins(10, 150, 10, 10) // Larger top margin
        } else {
            params.setMargins(10, 10, 10, 10) // Regular margin
        }
        holder.button.text = items[position] // 这里可以根据 weeks 更新按钮文本
        // 例如：holder.button.text = "Weeks: $weeks" // 如果需要显示周数
    }

    override fun getItemCount() = items.size

    fun updateItems(newItems: MutableList<String>) {
        items = newItems
        notifyDataSetChanged()
        Log.d("updateItemDebug", "updateItems: ")
    }
}