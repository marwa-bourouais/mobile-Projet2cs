package com.waelkhelil.sayara_dz.view.configure_version

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.waelkhelil.sayara_dz.R
import com.waelkhelil.sayara_dz.database.model.PaintColor
import kotlinx.android.synthetic.main.fragment_configure_version.*
import com.google.android.material.chip.ChipDrawable



class ConfigureDialogFragment : DialogFragment() {

    companion object {
        const val TAG: String = "ConfigureDialogFragment"
        fun newInstance() = ConfigureDialogFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_configure_version, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val versionId = arguments?.getInt("versionId")

        val toolbar: Toolbar = view.findViewById(R.id.toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_clear_black_24dp)
        toolbar.subtitle = versionId.toString() // set the subtitle first
        toolbar.title = getString(R.string.configure_it)

        dialog?.setOnKeyListener { _, i, keyEvent ->
            if (keyEvent.action == KeyEvent.ACTION_UP && i == KeyEvent.KEYCODE_BACK) {
                context?.let { exitConfirmation(it) }
            }
            true
        }
        toolbar.setNavigationOnClickListener {
            context?.let { exitConfirmation(it) }
        }
        val colorsList:List<PaintColor> = listOf(
            PaintColor("red", "#2196F3",0),
            PaintColor("blue", "#FF6050",100),
            PaintColor("green", "#FF0E83",200),
            PaintColor("yellow", "#839BFD",200),
            PaintColor("white", "#DDE3FE",400)
        )
        context?.let { initColorsChips(it, colorsList) }
    }
    private fun initColorsChips(context: Context, pColors:List<PaintColor>){
        chip_group.isSingleSelection = true
        for (lColor in pColors){
            val chip = Chip(context)
//            val chipDrawable =
//                ChipDrawable.createFromAttributes(context, null, 0, R.style.Widget_MaterialComponents_Chip_Choice)
//            chip.setChipDrawable(chipDrawable)
            chip.isCheckable = true

            val bitmap: Bitmap = Bitmap.createBitmap(500, 500, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            val left = 0
            val top = 0
            val right = 500
            val bottom = 500
            val shapeDrawable = ShapeDrawable(OvalShape())
            shapeDrawable.setBounds( left, top, right, bottom)
            shapeDrawable.paint.color = Color.parseColor(lColor.hexCode)
            shapeDrawable.draw(canvas)

            chip.chipIcon = shapeDrawable
            chip.text = lColor.name
            chip_group.addView(chip)
        }
    }

    override fun onStart() {
        super.onStart()
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog?.window?.setLayout(width, height)
    }
    private fun exitConfirmation(context: Context){
        MaterialAlertDialogBuilder(context)
            .setTitle(getString(R.string.exit_confguration))
            .setMessage(getString(R.string.exit_conf_confimation_msg))
            .setPositiveButton(getString(R.string.exit)) { _, _ ->
                dismissAllowingStateLoss()
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }
}
