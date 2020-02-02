/*
 * Copyright 2020 New Vector Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package im.vector.matrix.android.internal.crypto.verification.qrcode

/**
 * Ref: https://github.com/uhoreg/matrix-doc/blob/qr_key_verification/proposals/1543-qr_code_key_verification.md#qr-code-format
 */
data class QrCodeData(
        val userId: String,
        // Request Id. Can be an arbitrary value. In DM, it will be the event ID of the associated verification request event.
        val requestId: String,
        // The action
        val action: String,
        // key_<key_id>: each key that the user wants verified will have an entry of this form, where the value is the key in unpadded base64.
        // The QR code should contain at least the user's master cross-signing key. In the case where a device does not have a cross-signing key
        // (as in the case where a user logs in to a new device, and is verifying against another device), thin the QR code should contain at
        // least the device's key.
        val keys: Map<String, String>,
        // random single-use shared secret in unpadded base64. It must be at least 256-bits long (43 characters when base64-encoded).
        val sharedSecret: String,
        // the other user's master cross-signing key, in unpadded base64. In other words, if Alice is displaying the QR code,
        // this would be the copy of Bob's master cross-signing key that Alice has.
        val otherUserKey: String?,
        // The other device's key, in unpadded base64
        // This is only needed when a user is verifying their own devices, where the other device has not yet been signed with the cross-signing key.
        val otherDeviceKey: String?
) {
    companion object {
        const val ACTION_VERIFY = "verify"
    }
}