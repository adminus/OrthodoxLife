/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.telegram.messenger.exoplayer.upstream;

import org.telegram.messenger.exoplayer.C;
import org.telegram.messenger.exoplayer.util.Assertions;

import android.net.Uri;

import java.util.Arrays;

/**
 * Defines a region of media data.
 */
public final class DataSpec {

  /**
   * Permits an underlying network stack to request that the server use gzip compression.
   * <p>
   * Should not typically be set if the data being requested is already compressed (e.g. most audio
   * and video requests). May be set when requesting other data.
   * <p>
   * When a {@link DataSource} is used to request data with this flag set, and if the
   * {@link DataSource} does make a network request, then the value returned from
   * {@link DataSource#open(DataSpec)} will typically be {@link C#LENGTH_UNBOUNDED}. The data read
   * from {@link DataSource#read(byte[], int, int)} will be the decompressed data.
   */
  public static final int FLAG_ALLOW_GZIP = 1;

  /**
   * Identifies the source from which data should be read.
   */
  public final Uri uri;
  /**
   * Body for a POST request, null otherwise.
   */
  public final byte[] postBody;
  /**
   * The absolute position of the data in the full stream.
   */
  public final long absoluteStreamPosition;
  /**
   * The position of the data when read from {@link #uri}.
   * <p>
   * Always equal to {@link #absoluteStreamPosition} unless the {@link #uri} defines the location
   * of a subset of the underyling data.
   */
  public final long position;
  /**
   * The length of the data. Greater than zero, or equal to {@link C#LENGTH_UNBOUNDED}.
   */
  public final long length;
  /**
   * A key that uniquely identifies the original stream. Used for cache indexing. May be null if the
   * {@link DataSpec} is not intended to be used in conjunction with a cache.
   */
  public final String key;
  /**
   * Request flags. Currently {@link #FLAG_ALLOW_GZIP} is the only supported flag.
   */
  public final int flags;

  /**
   * Construct a {@link DataSpec} for the given uri and with {@link #key} set to null.
   *
   * @param uri {@link #uri}.
   */
  public DataSpec(Uri uri) {
    this(uri, 0);
  }

  /**
   * Construct a {@link DataSpec} for the given uri and with {@link #key} set to null.
   *
   * @param uri {@link #uri}.
   * @param flags {@link #flags}.
   */
  public DataSpec(Uri uri, int flags) {
    this(uri, 0, C.LENGTH_UNBOUNDED, null, flags);
  }

  /**
   * Construct a {@link DataSpec} where {@link #position} equals {@link #absoluteStreamPosition}.
   *
   * @param uri {@link #uri}.
   * @param absoluteStreamPosition {@link #absoluteStreamPosition}, equal to {@link #position}.
   * @param length {@link #length}.
   * @param key {@link #key}.
   */
  public DataSpec(Uri uri, long absoluteStreamPosition, long length, String key) {
    this(uri, absoluteStreamPosition, absoluteStreamPosition, length, key, 0);
  }

  /**
   * Construct a {@link DataSpec} where {@link #position} equals {@link #absoluteStreamPosition}.
   *
   * @param uri {@link #uri}.
   * @param absoluteStreamPosition {@link #absoluteStreamPosition}, equal to {@link #position}.
   * @param length {@link #length}.
   * @param key {@link #key}.
   * @param flags {@link #flags}.
   */
  public DataSpec(Uri uri, long absoluteStreamPosition, long length, String key, int flags) {
    this(uri, absoluteStreamPosition, absoluteStreamPosition, length, key, flags);
  }

  /**
   * Construct a {@link DataSpec} where {@link #position} may differ from
   * {@link #absoluteStreamPosition}.
   *
   * @param uri {@link #uri}.
   * @param absoluteStreamPosition {@link #absoluteStreamPosition}.
   * @param position {@link #position}.
   * @param length {@link #length}.
   * @param key {@link #key}.
   * @param flags {@link #flags}.
   */
  public DataSpec(Uri uri, long absoluteStreamPosition, long position, long length, String key,
      int flags) {
    this(uri, null, absoluteStreamPosition, position, length, key, flags);
  }

  /**
   * Construct a {@link DataSpec} where {@link #position} may differ from
   * {@link #absoluteStreamPosition}.
   *
   * @param uri {@link #uri}.
   * @param postBody {@link #postBody}.
   * @param absoluteStreamPosition {@link #absoluteStreamPosition}.
   * @param position {@link #position}.
   * @param length {@link #length}.
   * @param key {@link #key}.
   * @param flags {@link #flags}.
   */
  public DataSpec(Uri uri, byte[] postBody, long absoluteStreamPosition, long position, long length,
      String key, int flags) {
    Assertions.checkArgument(absoluteStreamPosition >= 0);
    Assertions.checkArgument(position >= 0);
    Assertions.checkArgument(length > 0 || length == C.LENGTH_UNBOUNDED);
    this.uri = uri;
    this.postBody = postBody;
    this.absoluteStreamPosition = absoluteStreamPosition;
    this.position = position;
    this.length = length;
    this.key = key;
    this.flags = flags;
  }

  @Override
  public String toString() {
    return "DataSpec[" + uri + ", " + Arrays.toString(postBody) + ", " + absoluteStreamPosition
        + ", "  + position + ", " + length + ", " + key + ", " + flags + "]";
  }

}
