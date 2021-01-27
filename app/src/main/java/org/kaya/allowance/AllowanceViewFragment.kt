package org.kaya.allowance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class AllowanceViewFragment : Fragment() {
    private var listener: ListenerRegistration? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_allowance_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val emptyBalance = resources.getString(R.string.balanceValueEmpty)

        val balanceView = view.findViewById<TextView>(R.id.textview_balance)

        view.findViewById<FloatingActionButton>(R.id.fab_add_transaction).setOnClickListener {
            findNavController().navigate(R.id.action_go_to_transaction_input)
        }

        balanceView.text = emptyBalance
    }

    override fun onResume() {
        super.onResume()

        val v = view
        if (v != null) {

            val db = Firebase.firestore
            val document = db.collection("kayasAccount").document("vwLfCN3Djj9p8vrPXtte")

            val balanceView = v.findViewById<TextView>(R.id.textview_balance)
            val emptyBalance = resources.getString(R.string.balanceValueEmpty)

            listener = document.addSnapshotListener { value, error ->
                val balance: Number? = value?.get("balance") as Number?

                balanceView.text =
                    if (balance != null) resources.getString(
                        R.string.balanceValueFormat,
                        balance.toFloat()
                    )
                    else emptyBalance
            }
        }
    }

    override fun onPause() {
        super.onPause()

        listener?.remove()
    }
}
