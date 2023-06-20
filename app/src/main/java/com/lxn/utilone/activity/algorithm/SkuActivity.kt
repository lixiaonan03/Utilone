package com.lxn.utilone.activity.algorithm

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lxn.utilone.R
import com.lxn.utilone.activity.BaseActivity
import com.lxn.utilone.databinding.ActivityRvBinding
import com.lxn.utilone.databinding.ActivitySkuBinding
import com.lxn.utilone.databinding.ActivitySuanfaBinding

/**
 *  @author 李晓楠
 *  功能描述: sku 算法学习
 *  时 间： 2023/6/7 14:50
 */
class SkuActivity : BaseActivity() {

    companion object {
        fun startActivity(context: Context?) {
            val intent = Intent(context, SkuActivity::class.java)
            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            }
            context?.startActivity(intent)
        }
    }

    private lateinit var vb: ActivitySkuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vb = ActivitySkuBinding.inflate(layoutInflater)
        setContentView(vb.root)
        vb.linTop.topText.text = "SKU 算法学习的"

        //造点业务数据
        val mutableList: MutableList<Sku> = mutableListOf()
        //业务假数据的
        mutableList.add(Sku(1, 101, 1001, 300, 100))
        mutableList.add(Sku(1, 101, 1003, 301, 0))
        mutableList.add(Sku(2, 101, 1001, 302, 50))
        mutableList.add(Sku(3, 101, 1001, 303, 0))
        mutableList.add(Sku(1, 100, 1002, 304, 41))
        mutableList.add(Sku(1, 102, 1003, 305, 0))
        mutableList.add(Sku(1, 101, 1002, 305, 0))
        mutableList.add(Sku(2, 100, 1004, 306, 28))
        mutableList.add(Sku(2, 100, 1001, 307, 41))
        mutableList.add(Sku(3, 100, 1002, 308, 15))
        mutableList.add(Sku(3, 102, 1001, 310, 33))
        mutableList.add(Sku(3, 102, 1003, 311, 33))
        mutableList.add(Sku(3, 102, 1004, 312, 0))
        mutableList.add(Sku(2, 100, 1003, 313, 44))
        mutableList.add(Sku(3, 100, 1001, 314, 0))

        //拼装好的数据 类似万物中item
        data = Data(
                //颜色组那些
                arrayListOf(1, 2, 3),
                //发货时间二维维度的
                arrayListOf(100, 101, 102),
                //尺码的
                arrayListOf(1001, 1002, 1003, 1004),
                mutableList
        )

        handleData(data)

        initView()

    }

    private lateinit var data: Data

    /**
     *  UI界面处理的
     */
    private fun initView() {
        vb.rvColors.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        vb.rvSize.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        vb.rvSessions.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)

        vb.rvColors.adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            override fun onCreateViewHolder(
                    parent: ViewGroup,
                    viewType: Int
            ): RecyclerView.ViewHolder {
                val itemView = LayoutInflater.from(parent.context).inflate(
                        R.layout.holder_section,
                        parent, false
                )
                return object : RecyclerView.ViewHolder(itemView) {}
            }

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                //获取一个颜色属性值
                val color = data.colorList[position]
                val isEnable = isColorEnable(color, data)
                val button = holder.itemView.findViewById<TextView>(R.id.cb_section)
                button.text = color.toString()
                applyState(isEnable, button)

                if (data.state.selectColor == color) {
                    button.setTextColor(Color.RED)
                } else {
                    button.setTextColor(Color.BLACK)
                }
                button.setOnClickListener {
                    if (isEnable == 2) {
                        if (data.state.selectColor == color) {
                            data.state.selectColor = 0
                        } else {
                            data.state.selectColor = color
                        }
                        flush()
                    }

                }

            }


            override fun getItemCount(): Int {
                return data.colorList.size
            }

        }


        vb?.rvSize?.adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            override fun onCreateViewHolder(
                    parent: ViewGroup,
                    viewType: Int
            ): RecyclerView.ViewHolder {
                val itemView = LayoutInflater.from(parent.context).inflate(
                        R.layout.holder_section,
                        parent, false
                )
                return object : RecyclerView.ViewHolder(itemView) {}
            }

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                val size = data.sizeList[position]
                val isEnable = isSizeEnable(size, data)
                val button = holder.itemView.findViewById<TextView>(R.id.cb_section)
                button.text = size.toString()
                applyState(isEnable, button)
                if (data.state.selectSize == size) {
                    button.setTextColor(Color.RED)
                } else {
                    button.setTextColor(Color.BLACK)
                }
                button.setOnClickListener {
                    if (isEnable == 2) {
                        if (data.state.selectSize == size) {
                            data.state.selectSize = 0
                        } else {
                            data.state.selectSize = size
                        }
                        flush()
                    }

                }

            }


            override fun getItemCount(): Int {
                return data.sizeList.size
            }

        }

        vb?.rvSessions?.adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            override fun onCreateViewHolder(
                    parent: ViewGroup,
                    viewType: Int
            ): RecyclerView.ViewHolder {
                val itemView = LayoutInflater.from(parent.context).inflate(
                        R.layout.holder_section,
                        parent, false
                )
                return object : RecyclerView.ViewHolder(itemView) {}
            }

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                val session = data.sessions[position]
                val isEnable = isSessionEnable(session, data)
                val button = holder.itemView.findViewById<TextView>(R.id.cb_section)
                button.text = session.toString()
                applyState(isEnable, button)

                if (data.state.selectSession == session) {
                    button.setTextColor(Color.RED)
                } else {
                    button.setTextColor(Color.BLACK)
                }

                button.setOnClickListener {
                    if (isEnable == 2) {
                        if (data.state.selectSession == session) {
                            data.state.selectSession = 0
                        } else {
                            data.state.selectSession = session
                        }
                        flush()
                    }

                }

            }


            override fun getItemCount(): Int {
                return data.sessions.size
            }

        }


        vb?.rvProduct?.layoutManager = LinearLayoutManager(this)
        vb?.rvProduct?.adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            override fun onCreateViewHolder(
                    parent: ViewGroup,
                    viewType: Int
            ): RecyclerView.ViewHolder {
                val itemView = LayoutInflater.from(parent.context).inflate(
                        R.layout.holder_product,
                        parent, false
                )
                return object : RecyclerView.ViewHolder(itemView) {}
            }


            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                val tv: TextView = holder.itemView.findViewById(R.id.tv_product)
                val sku = data.skuList[position]
                val enable = isEnable(sku.id, data)
                applyState(enable, tv)
                tv.text = data.skuList[position].toString()
            }

            override fun getItemCount(): Int {
                return data.skuList.size
            }
        }
    }


    /**
     * 设置单个按钮的状态的
     * 0 不可选  1 售罄   2 可选
     */
    private fun applyState(isEnable: Int, button: TextView) {
        when (isEnable) {
            0 -> button.setBackgroundColor(Color.GRAY)
            1 -> button.setBackgroundColor(Color.BLUE)
            2 -> button.setBackgroundColor(Color.GREEN)
        }
    }

    fun flush() {
        vb?.rvColors?.adapter?.notifyDataSetChanged()
        vb?.rvSize?.adapter?.notifyDataSetChanged()
        vb?.rvSessions?.adapter?.notifyDataSetChanged()
        vb?.rvProduct?.adapter?.notifyDataSetChanged()
    }


    /**
     * 处理数据的
     */
    private fun handleData(data: Data) {
        //给每个属性创建一个set集合  集合中放的是跟这个属性值相关的sku
        val colorSet: HashMap<Int, HashSet<Int>> = hashMapOf()
        for (color in data.colorList) {
            if (colorSet[color] == null) {
                colorSet[color] = hashSetOf()
            }
        }
        val sizeSet = HashMap<Int, HashSet<Int>>()
        for (size in data.sizeList) {
            if (sizeSet[size] == null) {
                sizeSet[size] = HashSet()
            }
        }
        val sessionSet = HashMap<Int, HashSet<Int>>()
        for (session in data.sessions) {
            if (sessionSet[session] == null) {
                sessionSet[session] = HashSet()
            }
        }
        // 过滤出售罄的sku的ID
        data.state.soldout = data.skuList.filter {
            it.stock == 0
        }.map {
            it.id
        }.toHashSet()

        data.state.color = colorSet
        data.state.size = sizeSet
        data.state.session = sessionSet

        //遍历sku 把属性对应的sku 放进去
        //这样每个属性下就会挂一个set 其中放的就是这个属性下相关的sku
        for (sku in data.skuList) {
            data.state.color[sku.color]?.add(sku.id)
            data.state.size[sku.size]?.add(sku.id)
            data.state.session[sku.session]?.add(sku.id)
        }
    }


    /**
     * 处理颜色选中的状态  0 代表不可选 1 售罄 2可选
     */
    private fun isColorEnable(color: Int, data: Data): Int {
        //我有黑色,红色
        val result: HashSet<Int> = hashSetOf()
        result.addAll(data.state.color[color]!!)

        // result 代表着这个颜色属性值 对应的sku集合
        //如果选择了 session 则和原来的取个交集 就是邻接矩阵的根据选择2列做交集
        if (data.state.selectSession != 0) {
            //我有第一批,第二批,取交集
            result.retainAll(data.state.session[data.state.selectSession]!!)
        }
        //如果选择了尺寸 再进去一次交集处理
        if (data.state.selectSize != 0) {
            result.retainAll(data.state.size[data.state.selectSize]!!)
        }
        //... 无论有几种状态,取交集,有结果，这个按钮就是亮的，如果要处理缺货，同理
        //查询最后如果result没有值 就证明这个属性不能选择了没有这个相关的sku
        val isDisable = result.isEmpty()
        if (isDisable) {
            return 0
        }
        //在这一步检查 是否有售罄的sku 如果有售罄的sku 就把这个sku从result中移除  如果移除完了result为空 证明这个属性值已经售罄了
        result.removeAll(data.state.soldout)
        if (result.isEmpty()) {
            return 1
        }
        return 2
    }


    private fun isSizeEnable(size: Int, data: Data): Int {
        val result: HashSet<Int> = hashSetOf()
        result.addAll(data.state.size[size]!!)
        if (data.state.selectSession != 0) {
            result.retainAll(data.state.session[data.state.selectSession]!!)
        }
        if (data.state.selectColor != 0) {
            result.retainAll(data.state.color[data.state.selectColor]!!)
        }
        val isDisable = result.isEmpty()
        if (isDisable) {
            return 0
        }
        result.removeAll(data.state.soldout)
        if (result.isEmpty()) {
            return 1
        }

        return 2
    }

    private fun isSessionEnable(session: Int, data: Data): Int {
        val result: HashSet<Int> = hashSetOf()
        result.addAll(data.state.session[session]!!)
        if (data.state.selectSize != 0) {
            result.retainAll(data.state.size[data.state.selectSize]!!)
        }
        if (data.state.selectColor != 0) {
            result.retainAll(data.state.color[data.state.selectColor]!!)
        }
        val isDisable = result.isEmpty()
        if (isDisable) {
            return 0
        }
        result.removeAll(data.state.soldout)
        if (result.isEmpty()) {
            return 1
        }

        return 2
    }


    /**
     * 处理sku条目的
     */
    private fun isEnable(id: Int, data: Data): Int {
        if (data.state.soldout.contains(id)) {
            return 1
        }
        val result: HashSet<Int> = data.skuList.map { it.id }.toHashSet()
        if (data.state.selectSize != 0) {
            result.retainAll(data.state.size[data.state.selectSize]!!)
        }
        if (data.state.selectColor != 0) {
            result.retainAll(data.state.color[data.state.selectColor]!!)
        }
        if (data.state.selectSession != 0) {
            result.retainAll(data.state.session[data.state.selectSession]!!)
        }
        if (result.contains(id)) {
            return 2
        }
        return 0
    }

}


