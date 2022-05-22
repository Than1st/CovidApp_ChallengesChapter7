package com.than.covidapp_challengeschapter7.ui.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.than.covidapp_challengeschapter7.R
import com.than.covidapp_challengeschapter7.data.model.DetailCountryCases
import com.than.covidapp_challengeschapter7.data.room.FavoriteEntity
import com.than.covidapp_challengeschapter7.databinding.FragmentDetailBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class DetailFragment : Fragment() {
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DetailFragmentViewModel by viewModel()
    private val args: DetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dataDetail = args.dataDetail

        viewModel.checkFavorite(1, dataDetail.country)
        viewModel.dataFavoriteByCountry.observe(viewLifecycleOwner){
            if(it != null){
                if (it.country_name.contains(dataDetail.country)){
                    binding.btnFavorite.setImageResource(R.drawable.ic_baseline_favorite)
                }
            }
        }

        binding.apply {
            Glide.with(requireContext())
                .load(dataDetail.flag)
                .centerCrop()
                .into(ivFlag)
            tvCountry.text = dataDetail.country
            tvAllCasesNumber.text = dataDetail.cases.toString()
            tvActiveNumber.text = dataDetail.active.toString()
            tvDeathNumber.text = dataDetail.death.toString()
            tvRecoveredNumber.text = dataDetail.recovered.toString()
            btnFavorite.setOnClickListener {
                addFavorite(dataDetail)
            }
        }
    }

    private fun addFavorite(dataDetail: DetailCountryCases) {
        var isFavorite = false
        viewModel.dataFavoriteByCountry.observe(viewLifecycleOwner){
            if(it != null){
                if (it.country_name == dataDetail.country){
                    isFavorite = true
                }
            }
        }
        if (isFavorite){
            deleteFavorite(1, dataDetail.country)
        } else {
            viewModel.insertFavorite(
                FavoriteEntity(
                    null,
                    1,
                    dataDetail.country,
                    dataDetail.cases,
                    dataDetail.flag,
                    dataDetail.active,
                    dataDetail.death,
                    dataDetail.recovered
                )
            )
            binding.btnFavorite.setImageResource(R.drawable.ic_baseline_favorite)
            Toast.makeText(
                requireContext(),
                "${dataDetail.country} add to favorite!",
                Toast.LENGTH_SHORT
            ).show()
        }
        viewModel.checkFavorite(1, dataDetail.country)
    }

    private fun deleteFavorite(id_user: Int, country_name: String) {
        viewModel.deleteFavorite(id_user, country_name)
        binding.btnFavorite.setImageResource(R.drawable.ic_baseline_favorite_border)
        Toast.makeText(
            requireContext(),
            "$country_name Deleted From Favorite!",
            Toast.LENGTH_SHORT
        ).show()

    }

}