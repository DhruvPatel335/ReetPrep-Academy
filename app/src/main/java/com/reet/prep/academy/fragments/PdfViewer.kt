package com.reet.prep.academy.fragments

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import com.reet.prep.academy.R
import com.reet.prep.academy.databinding.FragmentPdfViewerBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PdfViewer.newInstance] factory method to
 * create an instance of this fragment.
 */
class PdfViewer : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentPdfViewerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString("pdfUrl")
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPdfViewerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.settings.allowFileAccessFromFileURLs = true
        binding.webView.settings.allowContentAccess = true
        binding.webView.settings.allowUniversalAccessFromFileURLs = true
        binding.webView.settings.databaseEnabled = true
        binding.webView.loadUrl("$param1")
        binding.webView.webViewClient = object : WebViewClient() {
            override fun onReceivedError(
                view: WebView?,
                errorCode: Int,
                description: String?,
                failingUrl: String?
            ) {
                Toast.makeText(requireContext(), "Loading Error $description", Toast.LENGTH_SHORT).show()
            }

            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                if (url != null) {
                    view?.loadUrl(url)
                }
                return true
            }

            override fun onLoadResource(view: WebView?, url: String?) {
                super.onLoadResource(view, url)
                val javascript = """
                 (function() {
        var driveToolbar = document.querySelector('.ndfHFb-c4YZDc-Wrql6b ndfHFb-c4YZDc-Wrql6b-Hyc8Sd'); // You may need to adjust the selector
        if (driveToolbar) {
            driveToolbar.style.display = 'none';
        }
    })();
            """.trimIndent()
                binding.webView.evaluateJavascript(javascript, null)
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                // Handle page started event here
            }
        }
    }
}