package com.fanonymous.ayocuci.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.fanonymous.ayocuci.R
import com.fanonymous.ayocuci.data.booking.BookingItem
import org.jetbrains.annotations.NotNull

class HistoryBookingAdapter : RecyclerView.Adapter<HistoryBookingAdapter.ViewHolder> {

    private var list: MutableList<BookingItem>
    private var context: Context

    constructor(context: Context, list: MutableList<BookingItem>) {
        this.list = list
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_history, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list.get(position)
        holder.txtIdbooking.text = "AC-" + data.idBooking
        holder.txtBooking.text = data.bookingJenis + " - " + data.bookingMerk
        holder.txtJenisService.text = data.bookingJenisService
        holder.txtTanggal.text = data.bookingTanggal
        holder.txtStatus.text = data.bookingStatus
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(@NotNull itemView: View) : RecyclerView.ViewHolder(itemView) {
        @BindView(R.id.txt_idbooking)
        lateinit var txtIdbooking: TextView

        @BindView(R.id.txt_booking)
        lateinit var txtBooking: TextView

        @BindView(R.id.txt_jenis_service)
        lateinit var txtJenisService: TextView

        @BindView(R.id.txt_tanggal)
        lateinit var txtTanggal: TextView

        @BindView(R.id.txt_status)
        lateinit var txtStatus: TextView

        init {
            ButterKnife.bind(this, itemView)
        }
    }

    fun delete() {
        val size: Int = list.size
        if (size > 0) {
            for (i in 0 until size) {
                list.removeAt(0)
            }
            notifyItemRangeChanged(0, size)
        }
    }
}