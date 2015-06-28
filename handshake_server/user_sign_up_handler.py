from tornado.ioloop import IOLoop
from tornado.web import RequestHandler, Application, url
import pycps
import json

class UserSignUpHandler(RequestHandler):
	def post(self):
		user_id = self.get_argument('user_id')
		con = pycps.Connection('tcp://cloud-us-0.clusterpoint.com:9007', 'users', 'vmmonkey@gmail.com', 'monkey', '100642')
		first_name = self.get_argument('first_name')
		last_name = self.get_argument('last_name')
		tag_line = self.get_argument('tag_line')
		picture_url = self.get_argument('picture_url')
		phone_number = self.get_argument('phone_number')
		email = self.get_argument('email')
		connections = []
		user = {
			user_id : {
				'connections':connections, 
				'profile':{
					'firstx_name':first_name, 
					'last_name':last_name, 
					'tag_line':tag_line, 
					'picture_url':picture_url,
					'phone_number':phone_number,
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