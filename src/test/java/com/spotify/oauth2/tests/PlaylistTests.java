package com.spotify.oauth2.tests;

import com.spotify.oauth2.api.applicationApi.PlaylistApi;
import com.spotify.oauth2.pojo.Error;
import com.spotify.oauth2.pojo.Playlist;
import com.spotify.oauth2.utils.DataLoader;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class PlaylistTests {

    @Test
    public void shouldBeAbleToCreateAPlaylist() {
        Playlist requestPlaylist = playlistBuilder("New Playlist", "New playlist description", false);
        Response response = PlaylistApi.post(requestPlaylist);
        assertStatusCode(response.getStatusCode(), 201);
        assertPlaylistEqual(requestPlaylist, response.as(Playlist.class));
    }

    @Test
    public void shouldBeAbleToGetAPlaylist() {
        Playlist requestPlaylist = playlistBuilder("New Playlist", "New playlist description", false);
        Response response = PlaylistApi.get(DataLoader.getInstance().getGetPlaylistId());
        assertStatusCode(response.getStatusCode(), 200);
        assertPlaylistEqual(requestPlaylist, response.as(Playlist.class));
    }

    @Test
    public void shouldBeAbleToUpdateAPlaylist() {
        Playlist requestPlaylist = playlistBuilder("Updated Playlist Name", "Updated playlist description", false);
        Response response = PlaylistApi.update(DataLoader.getInstance().getUpdatePlaylistId(), requestPlaylist);
        assertStatusCode(response.getStatusCode(), 200);
    }

    @Test
    public void shouldNotBeAbleToCreateAPlaylistWithoutName() {
        Playlist requestPlaylist = playlistBuilder("", "New playlist description", false);
        Response response = PlaylistApi.post(requestPlaylist);
        assertStatusCode(response.getStatusCode(), 400);
        assertError(response.as(Error.class), 400, "Missing required field: name");
    }

    @Test
    public void shouldNotBeAbleToCreateAPlaylistWithExpiredToken() {
        String invalid_token = "12345";
        Playlist requestPlaylist = playlistBuilder("New Playlist", "New playlist description", false);
        Response response = PlaylistApi.post(invalid_token, requestPlaylist);
        assertStatusCode(response.getStatusCode(), 401);
        assertError(response.as(Error.class), 401, "Invalid access token");
    }

    public Playlist playlistBuilder(String name, String description, boolean _public) {
        return Playlist.builder().
                name(name).
                description(description).
                _public(_public).
                build();
    }

    public void assertPlaylistEqual(Playlist requestPlaylist, Playlist responsePlaylist) {
        assertThat(responsePlaylist.getName(), equalTo(requestPlaylist.getName()));
        assertThat(responsePlaylist.getDescription(), equalTo(requestPlaylist.getDescription()));
        assertThat(responsePlaylist.get_public(), equalTo(requestPlaylist.get_public()));
    }

    public void assertStatusCode(int actualStatusCode, int expectedStatusCode) {
        assertThat(actualStatusCode, equalTo(expectedStatusCode));
    }

    public void assertError(Error responseErr, int expectedStatusCode, String expectedMsg) {
        assertStatusCode(responseErr.getInnerError().getStatus(), expectedStatusCode);
        assertThat(responseErr.getInnerError().getMessage(), equalTo(expectedMsg));
    }

}
