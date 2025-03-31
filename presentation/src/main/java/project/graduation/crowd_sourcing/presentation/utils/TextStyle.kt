package project.graduation.crowd_sourcing.presentation.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import project.graduation.crowd_sourcing.presentation.R

@Composable
fun textStyleLarge() = TextStyle(fontSize = dimensionResource(id = R.dimen.sp_large).value.sp)

@Composable
fun textStyleMedium() = TextStyle(fontSize = dimensionResource(id = R.dimen.sp_medium).value.sp)

@Composable
fun textStyleSmall() = TextStyle(fontSize = dimensionResource(id = R.dimen.sp_small).value.sp)


@Composable
fun textStyleTitle() = TextStyle(fontSize = dimensionResource(id = R.dimen.sp_title).value.sp)