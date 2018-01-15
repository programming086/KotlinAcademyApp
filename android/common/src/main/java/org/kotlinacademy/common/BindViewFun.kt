@file:Suppress("UNCHECKED_CAST")
package org.kotlinacademy.common

import android.support.annotation.IdRes
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.View

fun  <T: View> ViewHolder.bindView(@IdRes viewId: Int) = lazy { itemView.findViewById<T>(viewId) }