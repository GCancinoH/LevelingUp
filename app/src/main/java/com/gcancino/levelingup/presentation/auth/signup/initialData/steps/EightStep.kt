package com.gcancino.levelingup.presentation.auth.signup.initialData.steps

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.gcancino.levelingup.presentation.auth.signup.initialData.InitialDataViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState

@ExperimentalMaterial3Api
@ExperimentalPermissionsApi
@Composable
fun EightStep(
    viewModel: InitialDataViewModel,
    context: Context
) {
    val cameraPermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)
    val storagePermissionState = rememberMultiplePermissionsState(
        listOf(
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    )
    val photos by viewModel.photos.collectAsState()
    val gridState = rememberLazyGridState()

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        viewModel.addPhotos(uris)
    }

    LaunchedEffect(Unit) {
        cameraPermissionState.launchPermissionRequest()
        storagePermissionState.launchMultiplePermissionRequest()
    }

    Box(
        modifier = Modifier.fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Now, let's add some photos to see your progress over time",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { galleryLauncher.launch("image/*") },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                ),
                contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
            ) {
                Icon(
                    imageVector = Icons.Filled.Camera,
                    contentDescription = "",
                    modifier = Modifier.size(ButtonDefaults.IconSize)
                )
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text(text = "Choose from Gallery")
            }
            Spacer(modifier = Modifier.height(16.dp))
            LazyHorizontalGrid(
                state = gridState,
                rows = GridCells.Fixed(3),
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    count = photos.size,
                    key = { index -> photos[index] },
                    contentType = { index -> photos[index] },
                    itemContent = { index ->
                        PhotoItem(
                            uri = photos[index],
                            onDelete = { viewModel.removePhoto(photos[index]) }
                        )
                    }
                )
            }
        }

        Button(
            onClick = { viewModel.nextStep() },
            modifier = Modifier.fillMaxWidth()
                .align(Alignment.BottomEnd),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Black
            )
        ) {
            Text(text = "Save my data")
        }
    }
}

@Composable
fun PhotoItem(
    uri: Uri,
    onDelete: () -> Unit
) {
    Box() {
        AsyncImage(
            model = uri,
            contentDescription = null,
            modifier = Modifier.size(120.dp),
            contentScale = ContentScale.Crop
        )
        IconButton(
            onClick = onDelete,
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = "Delete Photo",
                tint = Color(0xFF860000)
            )
        }
    }

}

