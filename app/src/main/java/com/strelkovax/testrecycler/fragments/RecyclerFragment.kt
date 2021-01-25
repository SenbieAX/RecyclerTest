package com.strelkovax.testrecycler.fragments

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.strelkovax.testrecycler.R
import com.strelkovax.testrecycler.adapters.ElementAdapter
import com.strelkovax.testrecycler.pojo.Element
import kotlinx.android.synthetic.main.fragment_recycler.*
import kotlinx.coroutines.*
import kotlin.random.Random


class RecyclerFragment : Fragment() {

    private var elementsInList = 15
    private var spanCount = 2

    private var elements = arrayListOf<Element>()
    private var deleteElements = arrayListOf<Element>()

    private lateinit var adapter: ElementAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            for (i in 1..15) {
                elements.add(Element(i))
            }
        } else {
            elements =
                savedInstanceState.getParcelableArrayList<Element>("listOfElements") as ArrayList<Element>
        }
        adapter = ElementAdapter(requireContext())
        spanCount =
            if (requireActivity().resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                2
            } else {
                4
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_recycler, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter.onButtonClickListener = object : ElementAdapter.OnButtonClickListener {
            override fun onButtonClick(viewToAnimate: View, element: Element, position: Int) {
                adapter.remove(viewToAnimate, element, position)
                deleteElements.add(element)
            }
        }
        rvListElements.adapter = adapter
        rvListElements.layoutManager = GridLayoutManager(context, spanCount)
        adapter.elementList = elements
        val job = GlobalScope.launch(Dispatchers.IO) {
            while (true) {
                delay(5000)
                withContext(Dispatchers.Main) {
                    val random = Random.nextInt(elements.size + 1)
                    if (deleteElements.isEmpty()) {
                        adapter.elementList.add(random, Element(elementsInList + 1))
                        adapter.notifyItemInserted(random)
                        elementsInList++
                    } else {
                        val sorted = deleteElements.sortedByDescending { it.id }
                        val element = sorted.last()
                        deleteElements.remove(element)
                        adapter.elementList.add(random, element)
                        adapter.notifyItemInserted(random)
                    }
                }
            }
        }
        job.start()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList("listOfElements", elements)
    }
}