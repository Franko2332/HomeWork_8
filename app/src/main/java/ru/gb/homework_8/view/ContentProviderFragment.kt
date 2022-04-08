package ru.gb.homework_8.view


import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.telecom.Call
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import ru.gb.homework_8.R
import ru.gb.homework_8.databinding.ContactItemBinding
import ru.gb.homework_8.databinding.FragmentContactsBinding
import java.lang.Exception


class ContentProviderFragment : Fragment(), Caller {
    private val _adapter: ContactsAdapter? = ContactsAdapter(this)
    private var _binding: FragmentContactsBinding? = null
    private val binding get() = _binding!!
    private val REQUEST_CODE = 42
    private val REQUEST_CODE_FOR_CALLING = 24

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentContactsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermission()
    }

    private fun checkPermission() {
        context?.let {
            when {
                ContextCompat.checkSelfPermission(it, Manifest.permission.READ_CONTACTS)  ==
                        PackageManager.PERMISSION_GRANTED -> getContacts()


                shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS) -> {
                    AlertDialog.Builder(it)
                        .setTitle("Доступ к контактам")
                        .setMessage(
                            "В рамках учебного занятия на курсе" +
                                    " GB вам необоходимо предоставить доступ к контактам"
                        )
                        .setPositiveButton("Предоставить", {
                                _, _ -> requestPermission(REQUEST_CODE) })
                        .setNegativeButton("Не надо", {
                                dialog, _ -> dialog.dismiss() })
                        .create()
                        .show()
                }
                shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE) -> {
                    AlertDialog.Builder(it)
                        .setTitle("Доступ к вызовам")
                        .setMessage(
                            "В рамках учебного занятия на курсе" +
                                    " GB вам необоходимо предоставить доступ к функции вызова"
                        )
                        .setPositiveButton("Предоставить", { _, _ ->
                            requestPermission(REQUEST_CODE_FOR_CALLING)})
                        .setNegativeButton("Не надо", { dialog, _ ->
                            dialog.dismiss() })
                        .create()
                        .show()
                }
                else -> {
                    requestPermission(REQUEST_CODE)
                }
            }
        }
    }

    private fun requestPermission(requestCode: Int) {
        Log.e("req code", requestCode.toString())
        requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS,
                Manifest.permission.CALL_PHONE),
                requestCode)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CODE -> {
                if ((grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] ==
                    PackageManager.PERMISSION_GRANTED) || (grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                ) {
                    getContacts()
                } else {
                    AlertDialog.Builder(requireContext())
                        .setTitle("Объяснение")
                        .setMessage(
                            "Вы отказали в получении разрешений приложению , " +
                                    "работа может быть нарушена"
                        )
                        .setNegativeButton("Закрыть", { dialog, _ -> dialog.dismiss() })
                        .create()
                        .show()
                }
            }

        }
    }


    @SuppressLint("Range")
    private fun getContacts() {
        requireContext()?.let {
            val contentResolver = it.contentResolver
            val cursorWithContact = contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null, null, null,
                null
            )
            cursorWithContact?.let {
                _adapter?.let { a -> a.setItems(it) }
            }
            binding.contactsRecyclerView.apply {
                this.adapter = _adapter
                this.layoutManager = LinearLayoutManager(requireContext())
            }

        }

    }


    companion object {
        @JvmStatic
        fun newInstance() = ContentProviderFragment()
    }

    override fun call(phoneNumber: String) {
        val intent = Intent(Intent.ACTION_CALL)
        intent.setData(Uri.parse("tel:"+phoneNumber))
        try {
            startActivity(intent)
        }catch (ex: Exception) {
            Toast.makeText(requireContext(),"Your call failed... " + ex.message,
                Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

}