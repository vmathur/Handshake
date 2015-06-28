from tornado.ioloop import IOLoop
from tornado.web import RequestHandler, Application, url
import pycps
import json

class UserSignUpHandler(RequestHandler):
	def post(self):
		con = pycps.Connection('tcp://cloud-us-0.clusterpoint.com:9007', 'users', 'vmmonkey@gmail.com', 'monkey', '100642')

		user_id = self.get_argument('user_id')
		first_name = self.get_argument('first_name')
		last_name = self.get_argument('last_name')
		tag_line = self.get_argument('tag_line')
		picture_url = self.get_argument('picture_url')
		profile_url = self.get_argument('profile_url')
		phone_number = "555-333-2222"
		email = "hello@world.com"
		connections = []
		user = {
			user_id : {
				'connections':connections, 
				'profile':{
					'first_name':first_name, 
					'last_name':last_name, 
					'tag_line':tag_line, 
					'picture_url':picture_url,
					'profile_url':profile_url,
					'email':email
				}
			}
		}
		try:
			con.insert(user)
		except pycps.APIError as e:
			print(e)
		return
		self.finish()