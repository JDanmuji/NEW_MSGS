package com.msgs.tripstory.service;


import java.util.List;

import com.msgs.msgs.entity.tripstory.StoryComment;

public interface TripStoryService {

    List<StoryComment> storyCommentsList();

	public void commentInsert(StoryComment storyComment);
}
