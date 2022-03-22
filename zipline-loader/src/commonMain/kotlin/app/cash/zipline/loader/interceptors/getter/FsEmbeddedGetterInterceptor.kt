package app.cash.zipline.loader.interceptors.getter

import app.cash.zipline.loader.ZiplineFile
import app.cash.zipline.loader.ZiplineFile.Companion.toZiplineFile
import okio.ByteString
import okio.FileSystem
import okio.Path

/**
 * Get [ZiplineFile] from embedded fileSystem that ships with the app.
 */
class FsEmbeddedGetterInterceptor(
  private val embeddedDir: Path,
  private val embeddedFileSystem: FileSystem,
) : GetterInterceptor {
  override suspend fun get(id: String, sha256: ByteString, url: String): ZiplineFile? {
    val resourcePath = embeddedDir / sha256.hex()

    return when {
      embeddedFileSystem.exists(resourcePath) -> {
        embeddedFileSystem.read(resourcePath) {
          readByteString()
        }.toZiplineFile()
      }
      else -> {
        null
      }
    }
  }
}
