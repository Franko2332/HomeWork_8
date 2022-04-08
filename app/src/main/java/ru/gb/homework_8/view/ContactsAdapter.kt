package ru.gb.homework_8.view

import android.annotation.SuppressLint
import android.database.Cursor
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.gb.homework_8.R

class ContactsAdapter(private val caller: Caller) :
    RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder>() {
    private var items: Cursor? = null

    class ContactsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val container =
            itemView.findViewById<LinearLayout>(R.id.contacts_item_linear_layout_container)
        val phoneNumberTextView = itemView.findViewById<TextView>(R.id.contacts_phone_number)
        val nameTextView = itemView.findViewById<TextView>(R.id.contacts_text_view)


    }

    @SuppressLint("NotifyDataSetChanged")
    public fun setItems(cursor: Cursor) {
        items = cursor
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.contact_item,
            parent, false
        )
        return ContactsViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: ContactsViewHolder, position: Int) {
        val columnIndex = items?.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
        val phoneColumnIndex = items?.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
        items?.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
        items?.moveToPosition(position).let {
            if (columnIndex != null && phoneColumnIndex != null) {
                holder.nameTextView.text = items?.getString(
                    columnIndex
                )
                holder.phoneNumberTextView.text = items?.getString(
                    phoneColumnIndex
                )
            }
            holder.container.setOnClickListener {
                caller.call(holder.phoneNumberTextView.text.toString())
            }
        }

    }

    override fun getItemCount(): Int {
        items?.let { return it.count }
        return 0
    }
}