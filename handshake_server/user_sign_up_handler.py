from tornado.ioloop import IOLoop
from tornado.web import RequestHandler, Application, url

class UserSignUpHandler(RequestHandler):
	def post(self):
		user_id = self.get_argument('user_id')
		print user_id