package com.recep.hunt.utilis

import antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator

class CustomProgressTextAdapter : CircularProgressIndicator.ProgressTextAdapter {
    override fun formatText(currentProgress: Double): String {
        return currentProgress.toInt().toString() + "%"
    }
}