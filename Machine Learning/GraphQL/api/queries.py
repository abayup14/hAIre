from .models import Post
from ariadne import convert_kwargs_to_snake_case


@convert_kwargs_to_snake_case
def list_posts_resolver(obj, info):
    try:
        posts = [post.to_dict() for post in Post.query.filter(Post.description.like("%%")).all()]
        print(posts)
        payload = {
            "success": True,
            "posts": posts
        }
    except Exception as e:
        payload = {
            "success": False,
            "errors": [str(e)]
        }
    return payload


@convert_kwargs_to_snake_case
def get_post_resolver(obj, info, id):
    try:
        post = Post.query.get(id)
        payload = {
            "success": True,
            "post": post.to_dict()
        }
    except AttributeError:
        payload = {
            "success": False,
            "errors": [f"Post item matching {id} not found"]
        }
    return payload
