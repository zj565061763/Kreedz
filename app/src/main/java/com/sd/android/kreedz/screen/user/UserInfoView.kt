package com.sd.android.kreedz.screen.user

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.sd.android.kreedz.core.ui.AppTextColor
import com.sd.android.kreedz.core.ui.AppTheme
import com.sd.android.kreedz.feature.common.ui.ComCountryTextViewLarge
import com.sd.android.kreedz.feature.common.ui.ComTextLabelView
import com.sd.lib.compose.constraintlayout.goneIf

@Composable
fun UserInfoView(
  modifier: Modifier = Modifier,
  country: String?,
  countryName: String?,
  regDate: String,
  roles: List<String>,
  siteVisits: String,
  chatBoxComments: String,
) {
  ConstraintLayout(
    modifier = modifier
      .fillMaxWidth()
      .padding(8.dp)
  ) {
    val (refCountry, refInfo, refRegDate) = createRefs()

    // Country
    ComCountryTextViewLarge(
      country = country,
      text = countryName,
      modifier = Modifier.constrainAs(refCountry) {
        start.linkTo(parent.start)
        top.linkTo(parent.top)
      }
    )

    // Reg date
    Text(
      text = "Since $regDate",
      fontSize = 12.sp,
      modifier = Modifier.constrainAs(refRegDate) {
        end.linkTo(parent.end)
        top.linkTo(parent.top)
        goneIf(regDate.isBlank())
      }
    )

    // Info
    InfoColumn(
      roles = roles,
      siteVisits = siteVisits,
      chatBoxComments = chatBoxComments,
      modifier = Modifier.constrainAs(refInfo) {
        width = Dimension.matchParent
        top.linkTo(refCountry.bottom, 6.dp)
      },
    )
  }
}

@Composable
private fun InfoColumn(
  modifier: Modifier = Modifier,
  roles: List<String>,
  siteVisits: String,
  chatBoxComments: String,
) {
  Column(
    modifier = modifier,
    verticalArrangement = Arrangement.spacedBy(6.dp),
  ) {
    if (roles.isNotEmpty()) {
      RolesView(roles = roles)
    }

    ExtraInfoView(
      siteVisits = siteVisits,
      chatBoxComments = chatBoxComments,
    )
  }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ExtraInfoView(
  modifier: Modifier = Modifier,
  siteVisits: String,
  chatBoxComments: String,
) {
  FlowRow(
    modifier = modifier,
    horizontalArrangement = Arrangement.spacedBy(16.dp),
    verticalArrangement = Arrangement.spacedBy(6.dp),
  ) {
    // Site visits
    ComTextLabelView(
      text = siteVisits,
      label = "site visits",
      textColor = AppTextColor.medium,
      textFontSize = 12.sp,
    )

    // ChatBox comments
    ComTextLabelView(
      text = chatBoxComments,
      label = "chatbox comments",
      textColor = AppTextColor.medium,
      textFontSize = 12.sp,
    )
  }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun RolesView(
  modifier: Modifier = Modifier,
  roles: List<String>,
) {
  FlowRow(
    modifier = modifier,
    verticalArrangement = Arrangement.spacedBy(6.dp),
    horizontalArrangement = Arrangement.spacedBy(6.dp),
  ) {
    roles.forEach { item ->
      Text(
        text = "( $item )",
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
      )
    }
  }
}

@Preview
@Composable
private fun Preview() {
  AppTheme {
    UserInfoView(
      country = "cn",
      countryName = "China",
      regDate = "11/01/2020",
      roles = listOf(
        "ADMIN",
        "RECORD HOLDER",
        "CUP ADMIN",
        "CEO",
      ),
      siteVisits = "15481",
      chatBoxComments = "52"
    )
  }
}

@Preview
@Composable
private fun PreviewEmptyCountry() {
  AppTheme {
    UserInfoView(
      country = null,
      countryName = null,
      regDate = "11/01/2020",
      roles = listOf(
        "ADMIN",
        "RECORD HOLDER",
        "CUP ADMIN",
        "CEO",
      ),
      siteVisits = "15481",
      chatBoxComments = "52"
    )
  }
}

@Preview
@Composable
private fun PreviewEmptyRoles() {
  AppTheme {
    UserInfoView(
      country = "cn",
      countryName = "China",
      regDate = "11/01/2020",
      roles = emptyList(),
      siteVisits = "15481",
      chatBoxComments = "52"
    )
  }
}