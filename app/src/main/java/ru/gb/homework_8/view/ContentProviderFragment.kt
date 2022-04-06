package ru.gb.homework_8.view


import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import ru.gb.homework_8.databinding.FragmentContactsBinding
import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.provider.ContactsContract
import android.widget.TextView
import ru.gb.homework_8.R


class ContentProviderFragment : Fragment() {
    private var _binding: FragmentContactsBinding? = null
    private val binding get() = _binding!!
    private val REQUEST_CODE = 42

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
                ContextCompat.checkSelfPermission(it, Manifest.permission.READ_CONTACTS) ==
                        PackageManager.PERMISSION_GRANTED -> getContacts()

                shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS) -> {
                    AlertDialog.Builder(it)
                        .setTitle("Доступ к контактам")
                        .setMessage(
                            "В рамках учебного занятия на курсе" +
                                    " GB вам необоходимо предоставить доступ к контактам"
                        )
                        .setPositiveButton("Предоставить", { _, _ -> requestPermission() })
                        .setNegativeButton("Не надо", { dialog, _ -> dialog.dismiss() })
                        .create()
                        .show()
                }
                else -> requestPermission()
            }
        }
    }

    private fun requestPermission() {
        requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS), REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CODE -> {
                if (grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    getContacts()
                } else {
                    AlertDialog.Builder(requireContext())
                        .setTitle("Объяснение")
                        .setMessage(
                            "В связи с непредоставлением доступа к контактам " +
                                    "экран будет пустой"
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
                ContactsContract.Contacts.CONTENT_URI,
                null, null, null,
                ContactsContract.Contacts.DISPLAY_NAME + " ASC"
            )
            cursorWithContact?.let { cursor ->
                for (i in 0..cursor.count) {
                    if (cursor.moveToPosition(i)) {
                        val name = cursor.getString(
                            cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
                        )
                        addView(it, name)
                    }
                }
            }
            cursorWithContact?.close()
        }

    }

    private fun addView(context: Context, name: String?) {
        binding.containerForContacts.addView(TextView(context).apply {
            text = name
            textSize = resources.getDimension(R.dimen.text_size)
        })
    }

    companion object {
        @JvmStatic
        fun newInstance() = ContentProviderFragment()
    }

}