package cst.unibucfmiif2026.ui.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Person2
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cst.unibucfmiif2026.R
import cst.unibucfmiif2026.ui.theme.UniBucFMIIF2026Theme
import cst.unibucfmiif2026.utils.isValidName
import cst.unibucfmiif2026.viewmodel.AddressDetailsViewModel

@Composable
fun AddressDetailsPage(viewModel: AddressDetailsViewModel = viewModel(), id : Long) {

    var firstname by remember { mutableStateOf("") }
    var lastname by remember { mutableStateOf("") }
    var firstnameError by remember { mutableStateOf<String?>(null) }
    var lastnameError by remember { mutableStateOf<String?>(null) }
    val invalidFirstnameMessage = stringResource(R.string.invalid_firstname)
    val invalidLastnameMessage = stringResource(R.string.invalid_lastname)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.address_details_title),
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = firstname,
            onValueChange = { newValue ->
                firstname = newValue
                firstnameError = null

            },
            label = {
                Text(stringResource(R.string.firstname))
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null
                )
            },
            singleLine = true,
            isError = firstnameError != null,
            supportingText = firstnameError?.let { errorMessage ->
                {
                    Text(errorMessage)
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = lastname,
            onValueChange = { newValue ->
                lastname = newValue
                lastnameError = null
            },
            label = {
                Text(stringResource(R.string.lastname))
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Person2,
                    contentDescription = null
                )
            },
            isError = lastnameError != null,
            supportingText = lastnameError?.let { errorMessage ->
                {
                    Text(errorMessage)
                }
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                var isValid = true
                if (!firstname.isValidName()) {
                    firstnameError = invalidFirstnameMessage
                    isValid = false
                }

                if (!lastname.isValidName()) {
                    lastnameError = invalidLastnameMessage
                    isValid = false
                }

                if(isValid) viewModel.addUser(firstname = firstname, lastname = lastname)
            },
            modifier = Modifier.fillMaxWidth(),
            // enabled = !isLoading
        ) {
//            when (isLoading) {
//                true -> CircularProgressIndicator(
//                    modifier = Modifier.size(24.dp),
//                    strokeWidth = 2.dp
//                )
//
//                false ->
//            }
            Text(stringResource(R.string.add_user))
        }

//        errorMessage?.let { errMsg ->
//            Spacer(modifier = Modifier.height(16.dp))
//            Text(
//                color = MaterialTheme.colorScheme.error,
//                style = MaterialTheme.typography.bodySmall,
//                fontWeight = FontWeight.Bold,
//                text = errMsg
//            )
//        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddressDetailsPagePreview() {
    UniBucFMIIF2026Theme {
        AddressDetailsPage(id = 0)
    }
}